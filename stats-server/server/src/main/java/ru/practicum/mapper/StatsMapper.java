package ru.practicum.mapper;

import ru.practicum.dto.StatsDto;
import ru.practicum.model.Stats;

import java.util.List;
import java.util.stream.Collectors;

public class StatsMapper {
    public static StatsDto statsToStatsDto(Stats stats) {
        return StatsDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .hits(Math.toIntExact(stats.getHits()))
                .build();
    }

    public static List<StatsDto> statsToStatsDtoCollection(List<Stats> statsCollection) {
        return statsCollection.stream().map(StatsMapper::statsToStatsDto).collect(Collectors.toList());
    }
}
