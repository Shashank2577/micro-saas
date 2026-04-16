package com.microsaas.interviewos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InterviewOSApplication {
    public static void main(String[] args) {
        SpringApplication.run(InterviewOSApplication.class, args);
    }
}
