package com.microsaas.pricingintelligence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PricingIntelligenceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PricingIntelligenceApplication.class, args);
    }
}
