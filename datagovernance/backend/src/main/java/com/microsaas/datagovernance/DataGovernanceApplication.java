package com.microsaas.datagovernance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.microsaas.datagovernance.repository")
@EntityScan(basePackages = "com.microsaas.datagovernance.model")
public class DataGovernanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataGovernanceApplication.class, args);
    }
}
