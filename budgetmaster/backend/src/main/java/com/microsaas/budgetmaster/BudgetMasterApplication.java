package com.microsaas.budgetmaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BudgetMasterApplication {
    public static void main(String[] args) {
        SpringApplication.run(BudgetMasterApplication.class, args);
    }
}
