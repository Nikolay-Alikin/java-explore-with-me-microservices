package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.event.client.EventClient;
import ru.yandex.practicum.user.client.UserClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(clients = {UserClient.class, EventClient.class})
@ComponentScan(basePackages = {"ru.yandex.practicum.request","client"})
public class RequestServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(RequestServiceApp.class);
    }
}
