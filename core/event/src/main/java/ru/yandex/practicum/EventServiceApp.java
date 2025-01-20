package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.category.client.CategoryClient;
import ru.yandex.practicum.location.LocationClient;
import ru.yandex.practicum.request.client.RequestClient;
import ru.yandex.practicum.user.client.UserClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(clients =
        {UserClient.class, CategoryClient.class, LocationClient.class, RequestClient.class})
@ComponentScan(basePackages = {"ru.yandex.practicum.event", "client"})
public class EventServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(EventServiceApp.class);
    }
}
