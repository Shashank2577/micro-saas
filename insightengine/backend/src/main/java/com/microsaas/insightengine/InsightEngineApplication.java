package com.microsaas.insightengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InsightEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(InsightEngineApplication.class, args);
    }
}
