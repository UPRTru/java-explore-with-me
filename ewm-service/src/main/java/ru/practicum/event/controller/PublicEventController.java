package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.constants.Constants;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.enums.EventSort;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/events")
@Validated
@RequiredArgsConstructor
public class PublicEventController {
    private final EventService eventService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventDto getEventById(@PathVariable("id") Long eventId, HttpServletRequest servlet) {
        return eventService.getEventByIdPublic(eventId, servlet);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<EventShortDto> getEventsPublic(@RequestParam(required = false) String text,
                        @RequestParam(required = false) List<Long> categories,
                        @RequestParam(required = false) Boolean paid,
                        @RequestParam(required = false) @DateTimeFormat(pattern = Constants.FORMAT_DATE_TIME)
                                                         LocalDateTime rangeStart,
                        @RequestParam(required = false) @DateTimeFormat(pattern = Constants.FORMAT_DATE_TIME)
                                                         LocalDateTime rangeEnd,
                        @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                        @RequestParam(required = false) String sort,
                        @RequestParam(defaultValue = "0") @Min(0) Integer from,
                        @RequestParam(defaultValue = "10") @Min(1) Integer size, HttpServletRequest servlet) {
        return eventService.getEventsByPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                PageRequest.of(from / size, size, Sort.by(EventSort.getSortField(sort)).ascending()), servlet);
    }
}
