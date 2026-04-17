package com.microsaas.competitorradar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CompetitorRadarApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompetitorRadarApplication.class, args);
    }
}
