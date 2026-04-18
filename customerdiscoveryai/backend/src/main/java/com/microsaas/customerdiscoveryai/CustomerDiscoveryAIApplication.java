package com.microsaas.customerdiscoveryai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CustomerDiscoveryAIApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerDiscoveryAIApplication.class, args);
    }
}
