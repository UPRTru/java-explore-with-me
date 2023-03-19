package ru.practicum.event.service;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.dto.StatsDto;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.enums.State;
import ru.practicum.event.enums.StateAction;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.QEvent;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.statclient.StatisticClient;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.dto.EventRequestStatusUpdate;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.maper.RequestMapper;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.request.status.RequestStatus;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

import static ru.practicum.event.mapper.EventMapper.*;
import static ru.practicum.request.maper.RequestMapper.requestsToDtoCollection;

@Service
public class EventServiceImp implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final StatisticClient statisticClient;

    public EventServiceImp(EventRepository eventRepository, UserRepository userRepository,
                           CategoryRepository categoryRepository, RequestRepository requestRepository,
                           StatisticClient statisticClient) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
        this.statisticClient = statisticClient;
    }

    @Override
    @Transactional
    public EventDto addNewEvent(Long userId, NewEventDto newEventDto) {
        User user = checkUser(userId);
        Category category = checkCategory(newEventDto.getCategory());
        if (LocalDateTime.parse(newEventDto.getEventDate(), formatter).isBefore(LocalDateTime.now().minusHours(2))) {
            throw new ConflictException("Нужно указать дату, которая еще не наступила. " + newEventDto.getEventDate());
        } else {
            Event newEvent = newDtoToEvent(newEventDto, user, category, LocalDateTime.now());
            return eventToDto(eventRepository.save(newEvent));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<EventShortDto> getPrivateUserEvents(Long userId, Pageable pageable) {
        checkUser(userId);
        return eventsPageToShortDtoCollection(
                setViews(eventRepository.findAllByInitiatorId(userId, pageable).toList()));
    }

    @Override
    @Transactional(readOnly = true)
    public EventDto getPrivateUserEvent(Long userId, Long eventId) {
        checkUser(userId);
        return eventToDto(setViews(List.of(checkEcent(eventId, userId))).get(0));
    }

    @Override
    @Transactional
    public EventDto updateEventUser(Long userId, Long eventId, UpdateEventRequest updateEventUserRequest) {
        checkUser(userId);
        Event event = checkEcent(eventId, userId);
        if (updateEventUserRequest.getEventDate() != null) {
            LocalDateTime eventTime = LocalDateTime.parse(updateEventUserRequest.getEventDate(), formatter);
            if (eventTime.isBefore(LocalDateTime.now().minusHours(2))) {
                throw new ConflictException("Нужно указать дату, которая еще не наступила. " + eventTime);
            } else {
                event.setEventDate(eventTime);
            }
        }
        if (event.getState() != null) {
            if (event.getState().equals(State.PUBLISHED)) {
                throw new ConflictException("Могут быть изменены только ожидающие или отмененные события");
            } else {
                event.setState(StateAction.getState(updateEventUserRequest.getStateAction()));
            }
        }
        if (updateEventUserRequest.getCategory() != null) {
            event.setCategory(checkCategory(updateEventUserRequest.getCategory()));
        }
        return eventToDto(setViews(List.of(updateEventUserToEvent(updateEventUserRequest, event))).get(0));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<EventDto> getEventsByAdmin(List<Long> ids, List<State> states,
                                                 List<Long> categories, LocalDateTime rangeStart,
                                                 LocalDateTime rangeEnd, Pageable pageable) {
        BooleanBuilder booleanBuilder = addQuery(ids, states, categories, rangeStart, rangeEnd);
        Page<Event> page;
        if (booleanBuilder.getValue() != null) {
            page = eventRepository.findAll(booleanBuilder, pageable);
        } else {
            page = eventRepository.findAll(pageable);
        }
        return eventsToDtoCollection(setViews(page.toList()));
    }

    @Override
    @Transactional
    public EventDto updateEventAdmin(Long eventId, UpdateEventRequest updateEventAdminRequest) {
        LocalDateTime eventTime;
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Событие с id=" + eventId + " не найдено"));
        if (updateEventAdminRequest.getEventDate() != null) {
            eventTime = LocalDateTime.parse(updateEventAdminRequest.getEventDate(), formatter);
            if (eventTime.isBefore(LocalDateTime.now().minusHours(1))) {
                throw new ConflictException("Нужно указать дату, которая еще не наступила. " + eventTime);
            } else {
                event.setEventDate(eventTime);
            }
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (event.getState() != null) {
                changeEventState(event, updateEventAdminRequest.getStateAction());
            } else {
                event.setState(StateAction.getState(updateEventAdminRequest.getStateAction()));
            }
        }
        if (updateEventAdminRequest.getCategory() != null) {
            event.setCategory(checkCategory(updateEventAdminRequest.getCategory()));
        }
        return eventToDto(eventRepository.save(updateEventUserToEvent(updateEventAdminRequest, event)));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<RequestDto> getUserEventRequests(Long userId, Long eventId) {
        checkUser(userId);
        Event event = checkEcent(eventId, userId);
        return requestsToDtoCollection(event.getRequests());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult approveRequests(Long userId, Long eventId, EventRequestStatusUpdate requests) {
        checkUser(userId);
        Event event = checkEcent(eventId, userId);
        if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new ConflictException("Лимит участников достигнут.");
        }
        List<RequestDto> confirmedRequests = new ArrayList<>();
        List<RequestDto> rejectedRequests = new ArrayList<>();
        moderationRequests(confirmedRequests, rejectedRequests, event, requests);
        return EventRequestStatusUpdateResult
                .builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }

    @Override
    @Transactional
    public EventDto getEventByIdPublic(Long eventId, HttpServletRequest servlet) {
        statisticClient.postStats(servlet);
        Event event = eventRepository.findByIdAndState(eventId, State.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + " не найдено"));
        return eventToDto(eventRepository.save(setViews(List.of(event)).get(0)));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<EventShortDto> getEventsByPublic(String text, List<Long> categories, Boolean paid,
                                                      LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                      Boolean onlyAvailable, Pageable pageable, HttpServletRequest servlet) {
        statisticClient.postStats(servlet);
        BooleanBuilder booleanBuilder = addQuery(null, null, categories, rangeStart, rangeEnd);
        Page<Event> page;
        if (text != null) {
            booleanBuilder.and(QEvent.event.annotation.likeIgnoreCase(text))
                    .or(QEvent.event.description.likeIgnoreCase(text));
        }
        if (rangeStart == null && rangeEnd == null) {
            booleanBuilder.and(QEvent.event.eventDate.after(LocalDateTime.now()));
        }
        if (onlyAvailable) {
            booleanBuilder.and((QEvent.event.participantLimit.eq(0)))
                    .or(QEvent.event.participantLimit.gt(QEvent.event.confirmedRequests));
        }
        if (paid != null) {
            booleanBuilder.and(QEvent.event.paid.eq(paid));
        }
        if (booleanBuilder.getValue() != null) {
            page = eventRepository.findAll(booleanBuilder.getValue(), pageable);
        } else {
            page = eventRepository.findAll(pageable);
        }
        return eventsPageToShortDtoCollection(setViews(page.toList()));
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));
    }

    private Event checkEcent(Long eventId, Long userId) {
        return eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + " не найдено"));
    }

    private Category checkCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с id: " + categoryId + " не найдена"));
    }

    private void moderationRequests(List<RequestDto> confirmedRequests,
                                    List<RequestDto> rejectedRequests,
                                    Event event, EventRequestStatusUpdate requests) {
        requestRepository.findAllByIdIn(requests.getRequestIds()).stream().peek(r -> {
            if (r.getStatus().equals(RequestStatus.PENDING)) {
                if (event.getParticipantLimit() == 0) {
                    r.setStatus(RequestStatus.CONFIRMED);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                } else if (event.getParticipantLimit() > event.getConfirmedRequests()) {
                    if (!event.getRequestModeration()) {
                        r.setStatus(RequestStatus.CONFIRMED);
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    } else {
                        if (requests.getStatus().equals(RequestStatus.CONFIRMED.toString())) {
                            r.setStatus(RequestStatus.CONFIRMED);
                            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                        } else {
                            r.setStatus(RequestStatus.REJECTED);
                        }
                    }
                } else {
                    r.setStatus(RequestStatus.REJECTED);
                }
            } else {
                throw new ConflictException("Может только подтверждать PENDING запросы.");
            }
        }).map(RequestMapper::requestToDto).forEach(r -> {
            if (r.getStatus().equals(RequestStatus.CONFIRMED)) {
                confirmedRequests.add(r);
            } else {
                rejectedRequests.add(r);
            }
        });
    }

    private void changeEventState(Event event, String actionState) {
        switch (StateAction.getState(actionState)) {
            case PUBLISHED:
                if (event.getState().equals(State.PENDING)) {
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                } else {
                    throw new ConflictException("не правильное состояние события:" + event.getState());
                }
            case CANCELED:
                if (event.getState().equals(State.PENDING)) {
                    event.setState(State.CANCELED);
                    break;
                } else {
                    throw new ConflictException("не правильное состояние события: " + event.getState());
                }
        }
    }

    private BooleanBuilder addQuery(List<Long> ids, List<State> states, List<Long> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (ids != null && !ids.isEmpty()) {
            booleanBuilder.and(QEvent.event.initiator.id.in(ids));
        }
        if (states != null && !states.isEmpty()) {
            booleanBuilder.and(QEvent.event.state.in(List.of(State.values())));
        }
        if (categories != null && !categories.isEmpty()) {
            booleanBuilder.and(QEvent.event.category.id.in(categories));
        }
        if (rangeStart != null) {
            booleanBuilder.and(QEvent.event.eventDate.after(rangeStart));
        }
        if (rangeEnd != null) {
            booleanBuilder.and(QEvent.event.eventDate.before(rangeEnd));
        }
        return booleanBuilder;
    }

    private List<Event> setViews(List<Event> events) {
        Map<String, Event> uriEvents = new HashMap<>();
        List<Long> eventsId = new ArrayList<>();
        for (Event event : events) {
            eventsId.add(event.getId());
            uriEvents.put("/events/" + event.getId(), event);
        }
        Set<Event> resp = new HashSet<>();
        List<StatsDto> statsDtoList;
        Map<Long, Integer> confRequests = (requestRepository.getConfirmedRequest(eventsId));
        statsDtoList = statisticClient.getViews(uriEvents.keySet());
        for (StatsDto statDto : statsDtoList) {
            String uri = statDto.getUri();
            Event event = uriEvents.get(uri);
            event.setViews(statDto.getHits());
            Integer confirmedRequest = confRequests.get(event.getId());
            event.setConfirmedRequests(confirmedRequest == null ? 0 : confirmedRequest);
            resp.add(event);
        }
        eventRepository.saveAll(resp);
        return new ArrayList<>(resp);
    }
}
