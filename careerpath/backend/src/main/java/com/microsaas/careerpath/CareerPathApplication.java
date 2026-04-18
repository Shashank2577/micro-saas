package com.microsaas.careerpath;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CareerPathApplication {
    public static void main(String[] args) {
        SpringApplication.run(CareerPathApplication.class, args);
    }
}
