package client;

import client.api.HitApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="${hit.name:hit}", url="${hit.url:http://localhost:9090}")
public interface HitApiClient extends HitApi {
}
