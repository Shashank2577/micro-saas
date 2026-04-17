package com.microsaas.taxoptimizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TaxOptimizerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaxOptimizerApplication.class, args);
    }
}
