package com.microsaas.documentvault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.microsaas.documentvault", "com.crosscutting.starter"})
public class DocumentVaultApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocumentVaultApplication.class, args);
    }
}
