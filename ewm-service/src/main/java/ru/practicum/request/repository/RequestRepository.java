package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.Map;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequesterId(Long userId);

    List<Request> findAllByIdIn(List<Long> requestIds);

    List<Request> findAllByRequesterIdAndEventId(Long userId, Long eventId);

    @Query("SELECT new map (req.event.id , count(req.id))  " +
            "FROM Request req WHERE req.event.id IN (?1)  AND req.status = 'CONFIRMED' " +
            "GROUP BY req.event.id")
    List<Map<Integer, Map<Long, Integer>>> getConfirmedRequestCount(List<Long> eventsId);
}
