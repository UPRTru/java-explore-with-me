package ru.practicum.event.mapper;

import org.springframework.data.domain.Page;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
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

    public static Event newDtoToEvent(NewEventDto newEventDto, User initiator, Category category, LocalDateTime createOn) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(), formatter))
                .location(newEventDto.getLocation())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .initiator(initiator)
                .createdOn(createOn)
                .build();
    }

    public static EventDto eventToDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(String.valueOf(event.getCreatedOn()))
                .description(event.getDescription())
                .eventDate(String.valueOf(event.getEventDate()))
                .initiator(UserMapper.userToDto(event.getInitiator()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(String.valueOf(event.getPublishedOn()))
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Collection<EventDto> eventsToDtoCollection(Page<Event> events) {
        return events.stream().map(EventMapper::eventToDto).collect(Collectors.toList());
    }

    public static EventShortDto eventToShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(String.valueOf(event.getEventDate()))
                .initiator(UserMapper.userToDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Collection<EventShortDto> eventsToShortDtoCollection(Collection<Event> events) {
        return events.stream().map(EventMapper::eventToShortDto).collect(Collectors.toList());
    }

    public static Collection<EventShortDto> eventsPageToShortDtoCollection(Page<Event> events) {
        return events.stream().map(EventMapper::eventToShortDto).collect(Collectors.toList());
    }

    public static Event updateEventUserToEvent(UpdateEventRequest updateEventRequest, Event event, boolean admin) {
        if (!updateEventRequest.getAnnotation().isEmpty()) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (!updateEventRequest.getDescription().isEmpty()) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (!updateEventRequest.getEventDate().isEmpty() && admin) {
            event.setEventDate(LocalDateTime.parse(updateEventRequest.getEventDate(), formatter));
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
        if (!updateEventRequest.getStateAction().isEmpty() && admin) {
            event.setState(State.valueOf(updateEventRequest.getStateAction()));
        }
        if (!updateEventRequest.getTitle().isEmpty()) {
            event.setTitle(updateEventRequest.getTitle());
        }
        return event;
    }
}
