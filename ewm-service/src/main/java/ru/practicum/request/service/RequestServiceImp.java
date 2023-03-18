package ru.practicum.request.service;

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
import java.util.Collection;

import static ru.practicum.request.maper.RequestMapper.requestToDto;
import static ru.practicum.request.maper.RequestMapper.requestsToDtoCollection;

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
    public void createRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + " не найдено"));
        User requester = userRepository.findById(userId)
                        .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));
        validateCreatingRequest(event, userId);
        if (event.getParticipantLimit() == 0 || event.getParticipantLimit() > event.getConfirmedRequests()) {
            Request createRequest = new Request();
            createRequest.setEvent(event);
            createRequest.setRequester(requester);
            createRequest.setCreated(LocalDateTime.now());
            if (!event.getRequestModeration()) {
                createRequest.setStatus(RequestStatus.CONFIRMED);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                eventRepository.save(event);
            } else {
                createRequest.setStatus(RequestStatus.PENDING);
            }
            requestRepository.save(createRequest);
        } else {
            throw new ConflictException("Достигнут лимит запросов");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<RequestDto> getUserRequests(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));
        return requestsToDtoCollection(requestRepository.findAllByRequesterId(userId));
    }

    @Override
    @Transactional
    public RequestDto canceledRequest(Long userId, Long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id: " + requestId + " не найден"));
        Event event = request.getEvent();
        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            request.setStatus(RequestStatus.CANCELED);
            eventRepository.save(event);
        } else {
            request.setStatus(RequestStatus.CANCELED);
        }
        return requestToDto(requestRepository.save(request));
    }

    private void validateCreatingRequest(Event event, Long userId) {
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Вы не можете создать запрос на участие в вашем собственном мероприятии");
        }
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException("Вы не можете участвовать в неопубликованном мероприятии");
        }
    }
}
