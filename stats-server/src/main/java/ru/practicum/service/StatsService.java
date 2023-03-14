package ru.practicum.service;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.Stats;

import java.util.List;

public interface StatsService {
    void saveHit(HitDto hitDto);

    List<Stats> getStats(String start, String end, List<String> uris, Boolean uniq);
}
