package ru.practicum.event.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.*;
import ru.practicum.event.enums.State;
import ru.practicum.event.model.Event;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;


@Slf4j
@UtilityClass
public class EventMapper {
    public static EventRequestDto adminToEventRequestDto(Event event) {
        return new EventRequestDto(event.getId(), event.getTitle(), event.getAnnotation(),
                CategoryMapper.categoryToDto(event.getCategory()), event.getPaid(),
                event.getEventDate(), UserMapper.userToDto(event.getInitiator()), event.getDescription(),
                event.getParticipantLimit(), State.valueOf(event.getState()).name(), event.getCreatedOn(),
                event.getLocation(), event.getRequestModeration());
    }

    public static Event newDtoToEvent(NewEventDto newEventDto, User initiator, Category category,
                                      LocalDateTime createOn) {
        return new Event(null, newEventDto.getAnnotation(), category, newEventDto.getDescription(),
                newEventDto.getEventDate(), newEventDto.getLocation(), newEventDto.getPaid(),
                newEventDto.getParticipantLimit(), newEventDto.getRequestModeration(), newEventDto.getTitle(),
                0, createOn, initiator, null, State.PENDING.name(), 0L, null,
                null);
    }

    public static EventDto eventToDto(Event event) {
        return new EventDto(event.getId(), event.getAnnotation(), CategoryMapper.categoryToDto(event.getCategory()),
                event.getConfirmedRequests(), event.getCreatedOn(), event.getDescription(), event.getEventDate(),
                event.getLocation(), UserMapper.userToDto(event.getInitiator()), event.getPaid(),
                event.getParticipantLimit(), event.getPublishedOn() != null ? event.getPublishedOn() : null,
                event.getRequestModeration(), State.valueOf(event.getState()).name(), event.getTitle(),
                event.getViews());
    }

    public static EventShortDto eventToShortDto(Event event) {
        return new EventShortDto(event.getId(), event.getAnnotation(),CategoryMapper.categoryToDto(event.getCategory()),
                event.getConfirmedRequests(), event.getEventDate(), UserMapper.userToDto(event.getInitiator()),
                event.getPaid(), event.getTitle(), event.getViews());
    }

    public static Collection<EventDto> eventsToDtoCollection(Collection<Event> events) {
        return events.stream().map(EventMapper::eventToDto).collect(Collectors.toList());
    }

    public static Collection<EventShortDto> eventsToShortDtoCollection(Collection<Event> events) {
        return events.stream().map(EventMapper::eventToShortDto).collect(Collectors.toList());
    }

    public static Collection<EventShortDto> eventsPageToShortDtoCollection(Collection<Event> events) {
        return events.stream().map(EventMapper::eventToShortDto).collect(Collectors.toList());
    }

    public static Event updateEventUserToEvent(UpdateEventRequest updateEventRequest, Event event) {
        if (updateEventRequest.getAnnotation() != null && !updateEventRequest.getAnnotation().isEmpty()) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getDescription() != null && !updateEventRequest.getDescription().isEmpty()) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getLocation() != null) {
            event.setLocation(updateEventRequest.getLocation());
        }
        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventRequest.getRequestModeration());
        }
        if (updateEventRequest.getTitle() != null && !updateEventRequest.getTitle().isEmpty()) {
            event.setTitle(updateEventRequest.getTitle());
        }
        log.info("Обновление события: {}", event);
        return event;
    }
}
