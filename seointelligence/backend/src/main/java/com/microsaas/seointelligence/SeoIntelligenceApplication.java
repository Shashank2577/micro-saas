package com.microsaas.seointelligence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SeoIntelligenceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeoIntelligenceApplication.class, args);
    }
}
