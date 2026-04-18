package com.microsaas.processminer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProcessMinerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProcessMinerApplication.class, args);
    }
}
