package com.github.artemlv.ewm.event.config;

import com.github.artemlv.ewm.exception.type.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClient;
import ru.yandex.practicum.client.StatsClient;
import ru.yandex.practicum.client.StatsClientImpl;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    @Value("${stats-server.url}")
    private String baseUrl;
    @Value("${stats-server.hit}")
    private String hit;
    @Value("${stats-server.stats}")
    private String stats;

    private final DiscoveryClient discoveryClient;

    @Bean
    public StatsClient statsClient() {
        var stats = discoveryClient.getInstances("stats").getFirst();
        if (ObjectUtils.isEmpty(this.stats) || ObjectUtils.isEmpty(hit) || ObjectUtils.isEmpty(baseUrl)) {
            throw new ConflictException("stats-server or hit or base url is empty");
        }

        return new StatsClientImpl(this.stats, hit,
                RestClient.builder()
                        .baseUrl(stats.getUri().toString())
                        .build()
        );
    }

}
