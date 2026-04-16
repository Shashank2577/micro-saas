package com.microsaas.auditready;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AuditReadyApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuditReadyApplication.class, args);
    }
}
