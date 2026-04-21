package com.microsaas.budgetpilot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.microsaas.budgetpilot", "com.crosscutting.starter"})
@EnableJpaRepositories(basePackages = "com.microsaas.budgetpilot.repository")
@EntityScan(basePackages = "com.microsaas.budgetpilot.model")
public class BudgetPilotApplication {
    public static void main(String[] args) {
        SpringApplication.run(BudgetPilotApplication.class, args);
    }
}
