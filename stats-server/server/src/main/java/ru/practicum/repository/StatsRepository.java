package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Hit;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Long> {
    @Query(value = "SELECT new ru.practicum.model.Stats (h.app, h.ip, h.uri, count(distinct h.ip))  " +
            "FROM Hit AS h WHERE h.timestamp > ?1 AND h.timestamp <?2 GROUP BY h.app, h.uri ,h.ip " +
            "ORDER BY count (h.app) DESC")
    List<Stats> getUniqueViewsWithoutUris(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.model.Stats (h.app, h.ip, h.uri, count(distinct h.ip))  " +
            "FROM Hit AS h WHERE h.timestamp > ?1 AND h.timestamp <?2 AND h.uri IN ?3 GROUP BY h.app, h.uri ,h.ip " +
            "ORDER BY count (h.uri) DESC")
    List<Stats> getUniqueViews(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "SELECT new ru.practicum.model.Stats (h.app,h.ip, h.uri, count(h.app)) " +
            "FROM Hit AS h where h.timestamp > ?1 AND h.timestamp <?2  GROUP BY h.app, h.uri,h.ip " +
            "ORDER BY count (h.app) DESC")
    List<Stats> getAllViewsWithoutUris(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.model.Stats (h.app, h.ip, h.uri, count(distinct h.ip))  " +
            "FROM Hit AS h WHERE h.timestamp > ?1 AND h.timestamp <?2 AND h.uri IN ?3 GROUP BY h.app, h.uri ,h.ip " +
            "ORDER BY count (h.uri) DESC")
    List<Stats> getAllViews(LocalDateTime start, LocalDateTime end, List<String> uris);
}
