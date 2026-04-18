package com.microsaas.insuranceai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InsuranceAIApplication {
    public static void main(String[] args) {
        SpringApplication.run(InsuranceAIApplication.class, args);
    }
}
