package com.microsaas.apievolver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class APIEvolverApplication {
    public static void main(String[] args) {
        SpringApplication.run(APIEvolverApplication.class, args);
    }
}
