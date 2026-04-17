package com.microsaas.dataqualityai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DataQualityAIApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataQualityAIApplication.class, args);
    }
}
