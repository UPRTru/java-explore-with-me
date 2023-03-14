package ru.practicum.dto;

import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HitMapper {
    public static Hit dtoToHit(HitDto hitDto) {
        return Hit
                .builder()
                .id(hitDto.getId())
                .app(hitDto.getApp())
                .ip(hitDto.getIp())
                .uri(hitDto.getUri())
                .timestamp(LocalDateTime.parse(hitDto.getTimestamp(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    public static Stats hitToStats(Hit hit) {
        return Stats
                .builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .build();
    }
}
