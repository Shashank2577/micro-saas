package com.microsaas.usageintelligence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UsageIntelligenceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UsageIntelligenceApplication.class, args);
    }
}
