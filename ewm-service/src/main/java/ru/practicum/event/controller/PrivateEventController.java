package ru.practicum.event.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.EventRequestStatusUpdate;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Validated
public class PrivateEventController {
    private final EventService eventService;

    public PrivateEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public void addEvent(@PathVariable @Min(1) Long userId, @RequestBody @Valid NewEventDto eventDto) {
        eventService.addNewEvent(userId, eventDto);
    }

    @GetMapping("{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public Collection<EventShortDto> getUserEvents(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                   @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                                   @PathVariable @Min(1) Long userId) {
        return eventService.getPrivateUserEvents(userId, PageRequest.of(from / size, size));
    }

    @GetMapping("{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventDto getUserEvent(@PathVariable @Min(1) Long userId, @PathVariable @Min(1) Long eventId) {
        return eventService.getPrivateUserEvent(userId, eventId);
    }

    @PatchMapping("{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventDto updateEvent(@PathVariable @Min(1) Long userId,
                                @PathVariable @Min(1) Long eventId,
                                @RequestBody @Valid UpdateEventRequest updateEvent) {
        return eventService.updateEventUser(userId, eventId, updateEvent);
    }

    @GetMapping("{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public Collection<RequestDto> getUserEventRequests(@PathVariable @Min(1) Long userId,
                                                       @PathVariable @Min(1) Long eventId) {
        return eventService.getUserEventRequests(userId, eventId);
    }

    @PatchMapping("{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult approveRequests(@PathVariable @Min(1) Long userId,
                                                          @PathVariable @Min(1) Long eventId,
                                                          @RequestBody EventRequestStatusUpdate requests) {
        return eventService.approveRequests(userId, eventId, requests);
    }
}