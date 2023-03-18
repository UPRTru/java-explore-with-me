package ru.practicum.service;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.model.StatsRequest;

import java.util.Collection;

public interface StatsService {
    void createHit(HitDto hitDto);

    Collection<StatsDto> getStats(StatsRequest request);
}
