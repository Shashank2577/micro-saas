package com.microsaas.featureflagai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {com.crosscutting.starter.flags.FlagsAutoConfiguration.class})
@EnableScheduling
public class FeatureFlagAIApplication {
    public static void main(String[] args) {
        SpringApplication.run(FeatureFlagAIApplication.class, args);
    }
}
