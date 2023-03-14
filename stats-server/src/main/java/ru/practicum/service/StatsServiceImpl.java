package ru.practicum.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.HitMapper;
import ru.practicum.dto.Stats;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.dto.HitMapper.dtoToHit;

@Service
@Transactional(readOnly = true)
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
    public List<Stats> getStats(String start, String end, List<String> uris, Boolean uniq) {
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        List<Stats> statsHits;
        if (Boolean.TRUE.equals(uniq)) {
            statsHits = statsRepository.findDistinct(startDate, endDate)
                    .stream()
                    .peek(stats -> stats.setHits(Long.valueOf(countStatsByUri(stats.getUri()))))
                    .collect(Collectors.toList());
        } else {
            statsHits = statsRepository.findAllByTimestampBetween(startDate, endDate)
                    .stream()
                    .map(HitMapper::hitToStats)
                    .peek(stats -> stats.setHits(Long.valueOf(countStatsByUri(stats.getUri()))))
                    .collect(Collectors.toList());
        }
        statsHits = uris == null ? statsHits : statsHits.stream()
                .map(stats -> filterByUris(stats, uris))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        statsHits.sort(Comparator.comparingLong(Stats::getHits)
                .thenComparingLong(Stats::getHits).reversed());
        return statsHits;
    }

    private Integer countStatsByUri(String uri) {
        return statsRepository.countByUri(uri);
    }

    private Stats filterByUris(Stats stats, List<String> uris) {
        if (uris.contains(stats.getUri())) {
            return stats;
        } else {
            return null;
        }
    }
}
