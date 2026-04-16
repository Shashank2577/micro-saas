package com.microsaas.taxdataorganizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TaxDataOrganizerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaxDataOrganizerApplication.class, args);
    }
}
