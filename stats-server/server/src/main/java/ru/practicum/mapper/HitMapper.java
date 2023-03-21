package ru.practicum.mapper;

import ru.practicum.dto.HitDto;
import ru.practicum.model.Hit;

public class HitMapper {
    public static Hit dtoToHit(HitDto hitDto) {
        return Hit.builder()
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .timestamp(hitDto.getTimestamp())
                .build();
    }
}
