package com.microsaas.realestateitel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RealEstateIntelApplication {
    public static void main(String[] args) {
        SpringApplication.run(RealEstateIntelApplication.class, args);
    }
}
