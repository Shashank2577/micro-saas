package com.microsaas.supportintelligence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SupportIntelligenceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SupportIntelligenceApplication.class, args);
    }
}
