package com.microsaas.educationos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EducationOSApplication {
    public static void main(String[] args) {
        SpringApplication.run(EducationOSApplication.class, args);
    }
}
