package ru.practicum.event.statclient;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.Client;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


@Slf4j
@Service
public class StatisticClient extends Client {
    private final String url;

    public StatisticClient(@Value("http://stats:9090") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
        url = serverUrl;
    }

    public void postStats(HttpServletRequest servlet) {
        log.info("Сохранение статистики.");
        post(new HitDto("ewm-service", servlet.getRequestURI(),
                servlet.getRemoteAddr(), LocalDateTime.now()));
    }

    public List<StatsDto> getViews(Set<String> ids) {
        log.info("Получение списка просмотров.");
        StringBuilder requestUris = new StringBuilder("/stats?");
        for (String uri : ids) {
            requestUris.append("&uris=").append(uri);
        }
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        var response = request.get(url + "/stats?" + requestUris);
        if (response == null || response.getBody().toString().isEmpty()) {
            return List.of();
        }
        return Arrays.asList(response.getBody().as(StatsDto[].class));
    }
}