package ru.practicum.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.model.Stats;
import ru.practicum.model.StatsRequest;
import ru.practicum.repository.StatsRepository;

import java.util.Collection;

import static ru.practicum.mapper.HitMapper.dtoToHit;
import static ru.practicum.mapper.StatsMapper.statsToStatsDtoCollection;

@Service
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    public StatsServiceImpl(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Transactional
    @Override
    public void saveHit(HitDto hitDto) {
        statsRepository.save(dtoToHit(hitDto));
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<StatsDto> getStats(StatsRequest request) {
        Collection<Stats> stats;
        if (Boolean.TRUE.equals(request.getUnique())) {
            stats = statsRepository.findUnique(request.getUris(), request.getStart(), request.getEnd());
        } else {
            stats = statsRepository.findStats(request.getUris(), request.getStart(), request.getEnd());
        }
        return statsToStatsDtoCollection(stats);
    }
}
