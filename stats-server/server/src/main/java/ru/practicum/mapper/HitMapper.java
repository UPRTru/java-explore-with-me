package ru.practicum.mapper;

import ru.practicum.dto.HitDto;
import ru.practicum.model.Hit;

public class HitMapper {
    public static Hit dtoToHit(HitDto hitDto) {
        return new Hit(null, hitDto.getApp(), hitDto.getUri(), hitDto.getIp(), hitDto.getTimestamp());
    }
}
