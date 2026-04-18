package com.microsaas.datagovernanceos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DataGovernanceOSApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataGovernanceOSApplication.class, args);
    }
}
