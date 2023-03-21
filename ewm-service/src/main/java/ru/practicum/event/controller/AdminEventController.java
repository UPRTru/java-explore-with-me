package ru.practicum.event.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.service.EventService;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
public class AdminEventController {
    private final EventService eventService;
    private static final String FORMATTER = "yyyy-MM-dd HH:mm:ss";

    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<EventDto> getEventsByFiltersForAdmin(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = FORMATTER) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = FORMATTER) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd,
                PageRequest.of(from / size, size));
    }

    @PatchMapping("{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventDto updateEventAdmin(@PathVariable @Min(1) Long eventId, @RequestBody UpdateEventRequest updateEvent) {
        return eventService.updateEventAdmin(eventId, updateEvent);
    }
}
