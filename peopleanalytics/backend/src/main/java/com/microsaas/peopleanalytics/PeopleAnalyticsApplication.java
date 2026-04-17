package com.microsaas.peopleanalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.microsaas.peopleanalytics", "com.crosscutting"})
@EnableJpaRepositories(basePackages = {"com.microsaas.peopleanalytics.repository"})
@EntityScan(basePackages = {"com.microsaas.peopleanalytics.model"})
@EnableScheduling
public class PeopleAnalyticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(PeopleAnalyticsApplication.class, args);
    }
}
