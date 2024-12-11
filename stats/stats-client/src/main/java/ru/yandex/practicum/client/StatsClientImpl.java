package ru.yandex.practicum.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    private final RestClient restClient;
    private final DiscoveryService discoveryService;

    public StatsClientImpl(DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
        this.restClient = RestClient.builder()
                .baseUrl(discoveryService.getServiceUrl("stats"))
                .build();
    }

    public List<StatCountHitsDto> get(final SearchStats searchStats) {
        Object[] listObj = restClient.get()
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
        return restClient.post()
                .uri(HIT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}
