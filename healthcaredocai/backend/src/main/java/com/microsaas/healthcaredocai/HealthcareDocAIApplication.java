package com.microsaas.healthcaredocai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HealthcareDocAIApplication {
    public static void main(String[] args) {
        SpringApplication.run(HealthcareDocAIApplication.class, args);
    }
}
