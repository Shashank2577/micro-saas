package com.microsaas.jobcraftai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobCraftAIApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobCraftAIApplication.class, args);
    }
}
