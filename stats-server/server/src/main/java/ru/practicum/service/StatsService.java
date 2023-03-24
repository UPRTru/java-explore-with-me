package ru.practicum.service;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.model.StatsRequest;

import java.util.List;

public interface StatsService {
    void saveHit(HitDto hitDto);

    List<StatsDto> getStats(StatsRequest request);
}
