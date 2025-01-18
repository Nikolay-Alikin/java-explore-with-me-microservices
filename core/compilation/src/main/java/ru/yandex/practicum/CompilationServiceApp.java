package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.yandex.practicum.event.client.EventClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(clients = {EventClient.class})
public class CompilationServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(CompilationServiceApp.class);
    }
}
