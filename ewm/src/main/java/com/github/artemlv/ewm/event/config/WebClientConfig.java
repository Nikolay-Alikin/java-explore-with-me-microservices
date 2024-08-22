package com.github.artemlv.ewm.event.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import ru.yandex.practicum.client.StatsClient;
import ru.yandex.practicum.client.StatsClientImpl;

@Configuration
public class WebClientConfig {
    @Value("${stats-server.url}")
    private String baseUrl;

    @Bean
    public StatsClient statsClient() {
        return new StatsClientImpl(RestClient.builder()
                .baseUrl(baseUrl)
                .build());
    }
}
