package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsDto {
    @JsonProperty("app")
    String app;
    @JsonProperty("uri")
    String uri;
    @JsonProperty("hits")
    Integer hits;
}
