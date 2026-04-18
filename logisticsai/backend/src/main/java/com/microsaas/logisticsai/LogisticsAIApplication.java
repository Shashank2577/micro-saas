package com.microsaas.logisticsai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LogisticsAIApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogisticsAIApplication.class, args);
    }
}
