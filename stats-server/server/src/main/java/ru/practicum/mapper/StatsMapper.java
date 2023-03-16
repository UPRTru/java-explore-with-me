package ru.practicum.mapper;

import ru.practicum.dto.StatsDto;
import ru.practicum.model.Stats;

import java.util.Collection;
import java.util.stream.Collectors;

public class StatsMapper {
    public static StatsDto statsToStatsDto(Stats stats){
        return StatsDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .hits(stats.getHits())
                .build();
    }

    public static Collection<StatsDto> statsToStatsDtoCollection(Collection<Stats> statsCollection){
        return statsCollection.stream().map(StatsMapper::statsToStatsDto).collect(Collectors.toList());
    }
}
