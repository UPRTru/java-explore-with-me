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
    @Query(name = "findAllViews", nativeQuery = true)
    List<Stats> findAllViews(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query(name = "findUniqueViews", nativeQuery = true)
    List<Stats> findUniqueViews(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query(name = "findAllViewsWithoutUris", nativeQuery = true)
    List<Stats> findAllViewsWithoutUris(LocalDateTime start, LocalDateTime end);

    @Query(name = "findUniqueViewsWithoutUris", nativeQuery = true)
    List<Stats> findUniqueViewsWithoutUris(LocalDateTime start, LocalDateTime end);
}
