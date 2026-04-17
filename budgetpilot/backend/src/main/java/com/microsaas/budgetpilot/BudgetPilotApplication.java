package com.microsaas.budgetpilot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BudgetPilotApplication {
    public static void main(String[] args) {
        SpringApplication.run(BudgetPilotApplication.class, args);
    }
}
