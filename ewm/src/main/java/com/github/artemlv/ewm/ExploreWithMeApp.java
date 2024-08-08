package com.github.artemlv.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ExploreWithMeApp {

    public static void main(String[] args) {
        SpringApplication.run(ExploreWithMeApp.class);
    }
}
