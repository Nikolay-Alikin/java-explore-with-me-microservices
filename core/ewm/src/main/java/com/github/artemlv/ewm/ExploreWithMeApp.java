package com.github.artemlv.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ru.yandex.practicum")
public class ExploreWithMeApp {

    public static void main(String[] args) {
        SpringApplication.run(ExploreWithMeApp.class);
    }
}

