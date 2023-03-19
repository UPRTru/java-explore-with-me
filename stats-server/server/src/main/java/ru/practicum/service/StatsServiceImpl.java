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
import static ru.practicum.mapper.StatsMapper.statsToDtoCollection;

@Service
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    public StatsServiceImpl(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Transactional
    @Override
    public void createHit(HitDto hitDto) {
        statsRepository.save(dtoToHit(hitDto));
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<StatsDto> getStats(StatsRequest request) {
        Collection<Stats> stats;
        if (request.getUris() == null || request.getUris().isEmpty()) {
            stats = (request.getUnique() ?
                    statsRepository.getUniqueViewsWithoutUris(request.getStart(), request.getEnd()) :
                    statsRepository.getAllViewsWithoutUris(request.getStart(), request.getEnd()));
        } else {
            stats = request.getUnique() ?
                    statsRepository.getUniqueViews(request.getStart(), request.getEnd(), request.getUris()) :
                    statsRepository.getAllViews(request.getStart(), request.getEnd(), request.getUris());
        }
        return statsToDtoCollection(stats);
    }
}
