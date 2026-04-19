package com.microsaas.legalresearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.microsaas.legalresearch.domain"})
@EnableJpaRepositories(basePackages = {"com.microsaas.legalresearch.repository"})
public class LegalResearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(LegalResearchApplication.class, args);
    }

}
