package com.microsaas.copyoptimizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CopyOptimizerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CopyOptimizerApplication.class, args);
    }
}
