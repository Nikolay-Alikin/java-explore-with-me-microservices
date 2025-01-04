package ru.yandex.practicum.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.NotFoundException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.MaxAttemptsRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClient;
import ru.yandex.practicum.SearchStats;
import ru.yandex.practicum.dto.CreateStatsDto;
import ru.yandex.practicum.dto.StatCountHitsDto;

@Lazy
@Service
public class StatsClientImpl implements StatsClient {

    private static final String HIT_PATH = "/hit";
    private static final String STATS_PATH = "/stats";

    private final DiscoveryClient discoveryClient;
    private final RetryTemplate retryTemplate;

    @Value("${stats-service.id}")
    private String statsServiceId;

    public StatsClientImpl(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
        RetryTemplate retryTemplate = new RetryTemplate();

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(3000L);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        MaxAttemptsRetryPolicy retryPolicy = new MaxAttemptsRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);
        this.retryTemplate = retryTemplate;
    }

    public List<StatCountHitsDto> get(final SearchStats searchStats) {
        Object[] listObj = RestClient.create()
                .get()
                .uri(uriBuilder -> uriBuilder.path(STATS_PATH)
                        .queryParam("start", searchStats.getStart())
                        .queryParam("end", searchStats.getEnd())
                        .queryParam("uris", searchStats.getUris())
                        .queryParam("unique", searchStats.isUnique())
                        .build())
                .retrieve()
                .body(Object[].class);

        if (ObjectUtils.isEmpty(listObj)) {
            return List.of();
        }

        ObjectMapper mapper = new ObjectMapper();
        return Arrays.stream(listObj)
                .map(object -> mapper.convertValue(object, StatCountHitsDto.class))
                .toList();
    }

    @Override
    public ResponseEntity<Void> save(final CreateStatsDto request) {
        return RestClient.create().post()
                .uri(makeUri(HIT_PATH))
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();

    }

    private ServiceInstance getInstance() {
        try {
            return discoveryClient
                    .getInstances(statsServiceId)
                    .getFirst();
        } catch (Exception exception) {
            throw new NotFoundException(
                    "Ошибка обнаружения адреса сервиса статистики с id: " + statsServiceId,
                    exception
            );
        }
    }

    private URI makeUri(String path) {
        ServiceInstance instance = retryTemplate.execute(cxt -> getInstance());
        return URI.create("http://" + instance.getHost() + ":" + instance.getPort() + path);
    }
}
