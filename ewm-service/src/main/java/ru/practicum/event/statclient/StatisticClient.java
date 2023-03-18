package ru.practicum.event.statclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.Client;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StatisticClient extends Client {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatisticClient(@Value("ewm-service") String application,
                           @Value("${stats-server.url}") String statsServiceUri,
                           ObjectMapper json) {
        super(application, statsServiceUri, json);
    }

    public void postStats(HttpServletRequest servlet, String app) {
        HitDto hit = HitDto
                .builder()
                .app(app)
                .ip(servlet.getRemoteAddr())
                .uri(servlet.getRequestURI())
                .timestamp(LocalDateTime.parse(LocalDateTime.now().format(formatter)))
                .build();
        saveHit("/hit", hit);
    }

    public Long getViews(String urlIn) {
        String url = urlIn;
        List<StatsDto> viewStatsList = loadStats(url);
        return viewStatsList != null && !viewStatsList.isEmpty() ? viewStatsList.get(0).getHits() : 0L;
    }
}