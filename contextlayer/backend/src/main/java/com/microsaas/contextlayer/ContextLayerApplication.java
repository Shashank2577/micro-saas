package com.microsaas.contextlayer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ContextLayerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContextLayerApplication.class, args);
    }
}
