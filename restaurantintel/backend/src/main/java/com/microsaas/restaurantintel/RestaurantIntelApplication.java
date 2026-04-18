package com.microsaas.restaurantintel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RestaurantIntelApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestaurantIntelApplication.class, args);
    }
}
