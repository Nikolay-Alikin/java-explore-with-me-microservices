package ru.yandex.practicum.client.api;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("stats")
public interface StatsClient extends StatsServiceApi {
}
