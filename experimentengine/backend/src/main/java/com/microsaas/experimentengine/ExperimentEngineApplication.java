package com.microsaas.experimentengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExperimentEngineApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExperimentEngineApplication.class, args);
    }
}
