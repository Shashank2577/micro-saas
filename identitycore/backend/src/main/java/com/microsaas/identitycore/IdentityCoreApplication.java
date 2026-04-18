package com.microsaas.identitycore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IdentityCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(IdentityCoreApplication.class, args);
    }
}
