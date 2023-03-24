package ru.practicum.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.enums.State;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.request.status.RequestStatus;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.request.maper.RequestMapper.requestToDto;
import static ru.practicum.request.maper.RequestMapper.requestsListToDtoList;

@Slf4j
@Service
public class RequestServiceImp implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public RequestServiceImp(RequestRepository requestRepository,
                             UserRepository userRepository, EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public RequestDto createRequest(Long userId, Long eventId) {
        Event event = checkEvent(eventId);
        User requester = checkUser(userId);
        if (!requestRepository.findAllByRequesterIdAndEventId(userId, eventId).isEmpty()) {
            log.info("Запрос с id пользователя: {}, id события: {} уже существует.", userId, eventId);
            throw new ConflictException("Запрос с id пользователя: " + userId + ", id события: "
                    + eventId + " уже существует.");
        }
        validateCreatingRequest(event, userId);
        if (event.getParticipantLimit() == 0 || event.getParticipantLimit() > event.getConfirmedRequests()) {
            Request createRequest = new Request(null, LocalDateTime.now(), event, requester, null);
            if (!event.getRequestModeration()) {
                createRequest.setStatus(RequestStatus.CONFIRMED.name());
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                eventRepository.save(event);
            } else {
                createRequest.setStatus(RequestStatus.PENDING.name());
            }
            log.info("Добавлен новый запрос в базу данных. {}", createRequest);
            return requestToDto(requestRepository.save(createRequest));
        } else {
            log.info("Достигнут лимит запросов. Событие id: {}", eventId);
            throw new ConflictException("Достигнут лимит запросов. Событие id: " + eventId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getUserRequests(Long userId) {
        checkUser(userId);
        log.info("Получение запроса пользователя с id: {}", userId);
        return requestsListToDtoList(requestRepository.findAllByRequesterId(userId));
    }

    @Override
    @Transactional
    public RequestDto canceledRequest(Long userId, Long requestId) {
        checkUser(userId);
        Request request = checkRequest(requestId);
        Event event = request.getEvent();
        if (request.getStatus().equals(RequestStatus.CONFIRMED.name())) {
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            request.setStatus(RequestStatus.CANCELED.name());
            eventRepository.save(event);
        } else {
            request.setStatus(RequestStatus.CANCELED.name());
        }
        log.info("Отмена запроса. {}", request);
        return requestToDto(requestRepository.save(request));
    }

    private void validateCreatingRequest(Event event, Long userId) {
        if (event.getInitiator().getId().equals(userId)) {
            log.info("Вы не можете создать запрос на участие в вашем собственном мероприятии. " +
                    "Пользователь id: {} Событие id: {}", userId, event.getId());
            throw new ConflictException("Вы не можете создать запрос на участие в вашем собственном мероприятии. " +
                    "Пользователь id: " + userId + " Событие id: " + event.getId());
        }
        if (!event.getState().equals(State.PUBLISHED.name())) {
            log.info("Вы не можете участвовать в неопубликованном мероприятии. " +
                    "Пользователь id: {} Событие id: {}", userId, event.getId());
            throw new ConflictException("Вы не можете участвовать в неопубликованном мероприятии. " +
                    "Пользователь id: " + userId + " Событие id: " + event.getId());
        }
    }

    private User checkUser(Long userId) {
        try {
            return userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));
        } catch (NotFoundException e) {
            log.info("Пользователь с id: {} не найден.", userId);
            throw new NotFoundException(e.getMessage());
        }
    }

    private Event checkEvent(Long eventId) {
        try {
            return eventRepository.findById(eventId)
                    .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + " не найдено."));
        } catch (NotFoundException e) {
            log.info("Событие с id: {} не найдено.", eventId);
            throw new NotFoundException(e.getMessage());
        }
    }

    private Request checkRequest(Long requestId) {
        try {
            return requestRepository.findById(requestId)
                    .orElseThrow(() -> new NotFoundException("Запрос с id: " + requestId + " не найден"));
        } catch (NotFoundException e) {
            log.info("Запрос с id: {} не найден.", requestId);
            throw new NotFoundException(e.getMessage());
        }
    }
}
