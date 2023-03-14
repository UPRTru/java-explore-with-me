package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.Stats;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StaticRepository extends JpaRepository<Hit, Long> {
    @Query("SELECT new ru.practicum.dto.Stats(h.app, h.uri, count(distinct h.ip)) " +
            "FROM Hit h WHERE h.timestamp between ?1 AND ?2")
    List<Stats> findDistinct(LocalDateTime start, LocalDateTime end);

    List<Hit> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);

    Integer countByUri(String uri);
}
