package com.microsaas.featureflagai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@org.springframework.data.jpa.repository.config.EnableJpaRepositories(basePackages = "com.microsaas.featureflagai")
@org.springframework.boot.autoconfigure.domain.EntityScan(basePackages = "com.microsaas.featureflagai")
@EnableScheduling
public class FeatureFlagAIApplication {
    public static void main(String[] args) {
        SpringApplication.run(FeatureFlagAIApplication.class, args);
    }
}
