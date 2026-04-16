package com.microsaas.localizationos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LocalizationOSApplication {
    public static void main(String[] args) {
        SpringApplication.run(LocalizationOSApplication.class, args);
    }
}
