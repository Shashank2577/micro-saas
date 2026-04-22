package com.microsaas.callintelligence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = {"com.microsaas.callintelligence.domain"})
@EnableJpaRepositories(basePackages = {"com.microsaas.callintelligence.domain"})
public class CallIntelligenceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CallIntelligenceApplication.class, args);
    }
}
