package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.request.model.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequesterId(Long userId);

    List<Request> findAllByIdIn(List<Long> requestIds);

    @Query("select new map (req.event.id , count(req.id))  " +
            " from Request req where req.event.id in (?1)  and req.status = 'CONFIRMED'" +
            " group by req.event.id")
    List<Map<Integer, Map<Long, Integer>>> getConfirmedRequestCount(List<Long> ids);

    default Map<Long, Integer> getConfirmedRequest(List<Long> ids) {
        var count = getConfirmedRequestCount(ids);
        Map<Long, Integer> result = new HashMap<>();
        for (Map e : count) {
            long id = (long) e.get("0");
            long confReq = (long) e.get("1");
            result.put(id, (int) confReq);
        }
        return result;
    }
}
