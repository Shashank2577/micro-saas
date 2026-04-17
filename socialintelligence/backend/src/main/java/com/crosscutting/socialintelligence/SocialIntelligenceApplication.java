package com.crosscutting.socialintelligence;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SocialIntelligenceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SocialIntelligenceApplication.class, args);
    }
}
