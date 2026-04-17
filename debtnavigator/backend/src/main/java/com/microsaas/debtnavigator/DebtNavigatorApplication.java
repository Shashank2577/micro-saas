package com.microsaas.debtnavigator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DebtNavigatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(DebtNavigatorApplication.class, args);
    }
}
