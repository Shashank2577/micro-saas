package com.microsaas.onboardflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OnboardFlowApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnboardFlowApplication.class, args);
    }
}
