package ru.practicum.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.enums.State;
import ru.practicum.request.dto.EventRequestStatusUpdate;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.RequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventService {
    EventDto addNewEvent(Long userId, NewEventDto eventDto);

    Collection<EventShortDto> getPrivateUserEvents(Long userId, Pageable pageable);

    EventDto getPrivateUserEvent(Long userId, Long eventId);

    EventDto updateEventUser(Long userId, Long eventId, UpdateEventRequest updateEvent);

    Collection<EventDto> getEventsByAdmin(List<Long> ids, List<State> states, List<Long> categories,
                                          LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    EventDto updateEventAdmin(Long eventId, UpdateEventRequest updateEvent);

    Collection<RequestDto> getUserEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult approveRequests(Long userId, Long eventId, EventRequestStatusUpdate requests);

    EventDto getEventByIdPublic(Long eventId, HttpServletRequest servlet);

    Collection<EventShortDto> getEventsByPublic(String text, List<Long> categories, Boolean paid,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                                Pageable pageable, HttpServletRequest servlet);
}
