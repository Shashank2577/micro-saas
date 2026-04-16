package com.microsaas.contractsense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ContractSenseApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContractSenseApplication.class, args);
    }
}
