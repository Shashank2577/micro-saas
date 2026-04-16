package com.microsaas.expenseintelligence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExpenseIntelligenceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExpenseIntelligenceApplication.class, args);
    }
}
