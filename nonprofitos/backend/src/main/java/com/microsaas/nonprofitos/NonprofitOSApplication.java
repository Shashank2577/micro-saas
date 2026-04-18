package com.microsaas.nonprofitos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NonprofitOSApplication {
    public static void main(String[] args) {
        SpringApplication.run(NonprofitOSApplication.class, args);
    }
}
