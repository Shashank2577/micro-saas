package com.microsaas.jobcraftai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = "com.microsaas.jobcraftai.model")
@EnableJpaRepositories(basePackages = "com.microsaas.jobcraftai.repository")
public class JobCraftAIApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobCraftAIApplication.class, args);
    }
}
