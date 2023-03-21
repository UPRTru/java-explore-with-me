package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.location.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    @NotBlank
    @Size(min = 1, max = 1000)
    String annotation;
    @NotNull
    Long category;
    @NotBlank
    @Size(min = 1, max = 4000)
    String description;
    @NotNull
    Location location;
    @NotNull
    Boolean paid;
    @NotBlank
    @Size(max = 120)
    String title;
    @PositiveOrZero
    Integer participantLimit;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    @NotNull
    Boolean requestModeration;
}
