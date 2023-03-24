package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.dto.constants.Constants;
import ru.practicum.event.location.Location;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestDto {
    Long id;
    String title;
    String annotation;
    CategoryDto category;
    Boolean paid;
    @JsonFormat(pattern = Constants.FORMAT_DATE_TIME)
    LocalDateTime eventDate;
    UserDto initiator;
    String description;
    Integer participantLimit;
    String state;
    @JsonFormat(pattern = Constants.FORMAT_DATE_TIME)
    LocalDateTime createdOn;
    Location location;
    Boolean requestModeration;
}
