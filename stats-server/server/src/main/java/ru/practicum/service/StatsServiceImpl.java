package ru.practicum.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.model.Stats;
import ru.practicum.model.StatsRequest;
import ru.practicum.repository.StatsRepository;

import java.util.List;

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
    public List<StatsDto> getStats(StatsRequest request) {
        List<Stats> stats;
        if (request.getUris() == null || request.getUris().isEmpty()) {
            stats = (request.getUnique() ?
                    statsRepository.findUniqueViewsWithoutUris(request.getStart(), request.getEnd()) :
                    statsRepository.findAllViewsWithoutUris(request.getStart(), request.getEnd()));
        } else {
            stats = request.getUnique() ?
                    statsRepository.findUniqueViews(request.getUris(), request.getStart(), request.getEnd()) :
                    statsRepository.findAllViews(request.getUris(), request.getStart(), request.getEnd());
        }
        return statsToStatsDtoCollection(stats);
    }
}
