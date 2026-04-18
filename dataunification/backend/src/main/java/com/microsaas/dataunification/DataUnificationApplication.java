package com.microsaas.dataunification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DataUnificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataUnificationApplication.class, args);
    }
}
