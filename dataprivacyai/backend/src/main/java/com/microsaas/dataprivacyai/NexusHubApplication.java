package com.microsaas.dataprivacyai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DataPrivacyAIApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataPrivacyAIApplication.class, args);
    }
}
