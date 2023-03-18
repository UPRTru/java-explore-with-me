package ru.practicum;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

@Service
public class Client {
    private final String statsServiceUri;
    private final ObjectMapper json;
    private final HttpClient httpClient;

    public Client(@Value("stat-service") String application,
                       @Value("services.stats-service.uri:http://localhost:9090")
                       String statsServiceUri,
                       ObjectMapper json) {
        this.statsServiceUri = statsServiceUri;
        this.json = json;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();
    }

    public void saveHit(String uri, HitDto hit) {
        try {
            HttpRequest.BodyPublisher bodyPublisher = HttpRequest
                    .BodyPublishers
                    .ofString(json.writeValueAsString(hit));
            HttpRequest hitRequest = HttpRequest.newBuilder()
                    .uri(URI.create(statsServiceUri + uri))
                    .POST(bodyPublisher)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .header(HttpHeaders.ACCEPT, "application/json")
                    .build();
            httpClient.send(hitRequest, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            System.out.println("Невозможно сохранить hit. " + e);
        }
    }

    public List<StatsDto> loadStats(String uri) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(statsServiceUri + uri))
                .GET()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.ACCEPT, "application/json")
                .build();
        List<StatsDto> statsDto = List.of();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String value = response.body();
                System.out.println(value);
                statsDto = json.readValue(value, new TypeReference<List<StatsDto>>(){});
            } else {
                System.out.println("Ошибка: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка запроса. " + e);
        }
        return statsDto;
    }
}