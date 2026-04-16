package com.microsaas.regulatoryfiling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RegulatoryFilingApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegulatoryFilingApplication.class, args);
    }
}
