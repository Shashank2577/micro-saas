package com.microsaas.equityintelligence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EquityIntelligenceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EquityIntelligenceApplication.class, args);
    }
}
