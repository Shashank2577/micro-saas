package com.microsaas.prospectiq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProspectIQApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProspectIQApplication.class, args);
    }
}
