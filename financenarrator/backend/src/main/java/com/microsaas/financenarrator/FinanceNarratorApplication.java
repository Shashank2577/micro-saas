package com.microsaas.financenarrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FinanceNarratorApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinanceNarratorApplication.class, args);
    }
}
