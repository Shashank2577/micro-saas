package com.microsaas.analyticsbuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AnalyticsBuilderApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnalyticsBuilderApplication.class, args);
    }
}
