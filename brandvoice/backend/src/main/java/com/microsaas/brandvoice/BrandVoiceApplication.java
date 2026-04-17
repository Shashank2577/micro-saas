package com.microsaas.brandvoice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BrandVoiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BrandVoiceApplication.class, args);
    }
}
