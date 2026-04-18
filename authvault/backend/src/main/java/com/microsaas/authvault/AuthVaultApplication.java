package com.microsaas.authvault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = {
    "com.microsaas.authvault.repository"
})
@EntityScan(basePackages = {
    "com.microsaas.authvault.entity"
})
public class AuthVaultApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthVaultApplication.class, args);
    }
}
