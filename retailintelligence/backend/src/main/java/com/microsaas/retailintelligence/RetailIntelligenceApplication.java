package com.microsaas.retailintelligence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RetailIntelligenceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RetailIntelligenceApplication.class, args);
    }
}
