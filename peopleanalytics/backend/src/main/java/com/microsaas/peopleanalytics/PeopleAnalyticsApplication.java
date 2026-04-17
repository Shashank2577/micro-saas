package com.microsaas.peopleanalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PeopleAnalyticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(PeopleAnalyticsApplication.class, args);
    }
}
