package com.micro_saas.featureflagai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class})
public class FeatureFlagAiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FeatureFlagAiApplication.class, args);
    }
}