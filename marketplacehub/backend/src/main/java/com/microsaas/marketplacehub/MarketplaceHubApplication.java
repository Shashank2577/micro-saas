package com.microsaas.marketplacehub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.microsaas.marketplacehub", "com.marketplacehub"})
@EntityScan("com.marketplacehub.model")
@EnableJpaRepositories("com.marketplacehub.repository")
public class MarketplaceHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketplaceHubApplication.class, args);
    }
}
