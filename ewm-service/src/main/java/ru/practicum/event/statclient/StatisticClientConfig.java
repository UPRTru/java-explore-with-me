package ru.practicum.event.statclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatisticClientConfig {
    @Value("http://localhost:9090")
    private String url;

    @Bean
    StatisticClient statisticClient() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return new StatisticClient(url, builder);
    }
}
