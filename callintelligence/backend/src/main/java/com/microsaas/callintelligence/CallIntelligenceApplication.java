package com.microsaas.callintelligence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CallIntelligenceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CallIntelligenceApplication.class, args);
    }
}
