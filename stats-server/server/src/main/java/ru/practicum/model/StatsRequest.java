package ru.practicum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.constants.Constants;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsRequest {
    @JsonFormat(pattern = Constants.FORMAT_DATE_TIME)
    LocalDateTime start;
    @JsonFormat(pattern = Constants.FORMAT_DATE_TIME)
    LocalDateTime end;
    List<String> uris;
    Boolean unique;
}
