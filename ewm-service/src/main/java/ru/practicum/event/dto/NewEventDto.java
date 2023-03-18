package ru.practicum.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.location.Location;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    @Size(min = 20, max = 1000)
    @NotBlank
    String annotation;
    @Min(1)
    @NotNull
    Long category;
    @Size(min = 20, max = 4000)
    @NotBlank
    String description;
    @NotBlank
    String eventDate;
    @NotNull
    Location location;
    Boolean paid;
    @Min(0)
    Integer participantLimit;
    Boolean requestModeration;
    @Size(min = 3, max = 120)
    @NotBlank
    String title;
}
