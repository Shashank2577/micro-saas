package com.microsaas.agencyos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AgencyosApplication {
    public static void main(String[] args) {
        SpringApplication.run(AgencyosApplication.class, args);
    }
}
