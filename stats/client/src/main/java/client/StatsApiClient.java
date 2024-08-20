package client;

import client.api.StatsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="${stats.name:stats}", url="${stats.url:http://localhost:9090}")
public interface StatsApiClient extends StatsApi {
}
