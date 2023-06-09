package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.dto.constants.Constants;
import ru.practicum.model.StatsRequest;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @GetMapping(path = "/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<StatsDto> getStats(@RequestParam(defaultValue = "2020-05-05 00:00:00")
                                         @DateTimeFormat(pattern = Constants.FORMAT_DATE_TIME) LocalDateTime start,
                                         @RequestParam(defaultValue = "2030-05-05 00:00:00")
                                         @DateTimeFormat(pattern = Constants.FORMAT_DATE_TIME) LocalDateTime end,
                                         @RequestParam(required = false) List<String> uris,
                                         @RequestParam(defaultValue = "false") Boolean unique) {
        return statsService.getStats(new StatsRequest(start, end, uris, unique));
    }

    @PostMapping(path = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createHit(@RequestBody HitDto hitDto) {
        statsService.saveHit(hitDto);
    }
}
