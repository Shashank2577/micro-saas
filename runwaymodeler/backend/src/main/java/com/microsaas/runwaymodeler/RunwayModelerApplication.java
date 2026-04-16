package com.microsaas.runwaymodeler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RunwayModelerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RunwayModelerApplication.class, args);
    }
}
