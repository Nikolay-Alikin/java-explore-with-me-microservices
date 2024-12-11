package ru.yandex.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
@RequiredArgsConstructor
public class DiscoveryService {

    private final DiscoveryClient discoveryClient;

    public String getServiceUrl(String serviceId) {
        var uri = discoveryClient.getInstances(serviceId).getFirst().getUri();
        return uri.toString();
    }
}
