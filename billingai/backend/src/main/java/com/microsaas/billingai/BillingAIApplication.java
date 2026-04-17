package com.microsaas.billingai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BillingAIApplication {
    public static void main(String[] args) {
        SpringApplication.run(BillingAIApplication.class, args);
    }
}
