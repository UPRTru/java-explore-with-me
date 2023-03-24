package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.HitDto;
import ru.practicum.model.Hit;

@UtilityClass
public class HitMapper {
    public static Hit dtoToHit(HitDto hitDto) {
        return new Hit(null, hitDto.getApp(), hitDto.getUri(), hitDto.getIp(), hitDto.getTimestamp());
    }
}
