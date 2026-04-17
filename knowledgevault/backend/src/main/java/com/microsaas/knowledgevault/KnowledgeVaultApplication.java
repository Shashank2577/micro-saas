package com.microsaas.knowledgevault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KnowledgeVaultApplication {
    public static void main(String[] args) {
        SpringApplication.run(KnowledgeVaultApplication.class, args);
    }
}
