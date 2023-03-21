package ru.practicum.event.mapper;

import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.*;
import ru.practicum.event.enums.State;
import ru.practicum.event.model.Event;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.stream.Collectors;

public class EventMapper {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EventRequestDto adminToEventRequestDto(Event event) {
        return EventRequestDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToDto(event.getCategory()))
                .paid(event.getPaid())
                .eventDate(event.getEventDate().format(formatter))
                .initiator(UserMapper.userToDto(event.getInitiator()))
                .description(event.getDescription())
                .participantLimit(event.getParticipantLimit())
                .state(State.valueOf(event.getState()))
                .createdOn(event.getCreatedOn().format(formatter))
                .location(event.getLocation())
                .requestModeration(event.getRequestModeration())
                .build();
    }

    public static Event newDtoToEvent(NewEventDto newEventDto, User initiator, Category category, LocalDateTime createOn) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate().format(formatter), formatter))
                .location(newEventDto.getLocation())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .initiator(initiator)
                .createdOn(LocalDateTime.parse(createOn.format(formatter), formatter))
                .confirmedRequests(0)
                .state(State.PENDING.name())
                .build();
    }

    public static EventDto eventToDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn().format(formatter))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(formatter))
                .initiator(UserMapper.userToDto(event.getInitiator()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn() != null ? event.getPublishedOn().format(formatter) : null)
                .requestModeration(event.getRequestModeration())
                .state(State.valueOf(event.getState()))
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Collection<EventDto> eventsToDtoCollection(Collection<Event> events) {
        return events.stream().map(EventMapper::eventToDto).collect(Collectors.toList());
    }

    public static EventShortDto eventToShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate().format(formatter))
                .initiator(UserMapper.userToDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
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
        return event;
    }
}
