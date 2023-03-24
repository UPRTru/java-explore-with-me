package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.constants.Constants;
import ru.practicum.event.location.Location;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventRequest {
    @Size(min = 20, max = 1000)
    String annotation;
    @Min(1)
    Long category;
    @Size(min = 20, max = 4000)
    String description;
    @JsonFormat(pattern = Constants.FORMAT_DATE_TIME)
    LocalDateTime eventDate;
    Location location;
    Boolean paid;
    @Min(0)
    Integer participantLimit;
    Boolean requestModeration;
    String stateAction;
    @Size(min = 3, max = 120)
    String title;
}
