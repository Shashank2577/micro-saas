package com.microsaas.jobcraftai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobcraftaiApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobcraftaiApplication.class, args);
    }
}
