package com.microsaas.contentos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ContentOSApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentOSApplication.class, args);
    }
}
