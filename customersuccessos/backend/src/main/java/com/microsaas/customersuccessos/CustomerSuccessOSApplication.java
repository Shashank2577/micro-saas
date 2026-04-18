package com.microsaas.customersuccessos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CustomerSuccessOSApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerSuccessOSApplication.class, args);
    }
}
