package com.microsaas.constructioniq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ConstructionIQApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConstructionIQApplication.class, args);
    }
}
