package ru.practicum.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.enums.State;
import ru.practicum.event.location.Location;
import ru.practicum.user.dto.UserDto;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestDto {
    Long id;
    String title;
    String annotation;
    CategoryDto category;
    Boolean paid;
    String eventDate;
    UserDto initiator;
    String description;
    Integer participantLimit;
    State state;
    String createdOn;
    Location location;
    Boolean requestModeration;
}
