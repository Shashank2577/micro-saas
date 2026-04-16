package com.microsaas.creatoranalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CreatorAnalyticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(CreatorAnalyticsApplication.class, args);
    }
}
