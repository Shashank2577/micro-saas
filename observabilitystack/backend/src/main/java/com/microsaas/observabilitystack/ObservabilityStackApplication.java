package com.microsaas.observabilitystack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ObservabilityStackApplication {
    public static void main(String[] args) {
        SpringApplication.run(ObservabilityStackApplication.class, args);
    }
}
