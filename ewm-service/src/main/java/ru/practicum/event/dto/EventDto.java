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
public class EventDto {
    Long id;
    String annotation;
    CategoryDto category;
    Integer confirmedRequests;
    @JsonFormat(pattern = Constants.FORMAT_DATE_TIME)
    LocalDateTime createdOn;
    String description;
    @JsonFormat(pattern = Constants.FORMAT_DATE_TIME)
    LocalDateTime eventDate;
    Location location;
    UserDto initiator;
    Boolean paid;
    Integer participantLimit;
    @JsonFormat(pattern = Constants.FORMAT_DATE_TIME)
    LocalDateTime publishedOn;
    Boolean requestModeration;
    String state;
    String title;
    Long views;
}
