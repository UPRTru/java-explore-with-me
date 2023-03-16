package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Hit;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Long> {
    @Query(name = "findUnique", nativeQuery = true)
    Collection<Stats> findUnique(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query(name = "findStats", nativeQuery = true)
    Collection<Stats> findStats(List<String> uris, LocalDateTime start, LocalDateTime end);
}
