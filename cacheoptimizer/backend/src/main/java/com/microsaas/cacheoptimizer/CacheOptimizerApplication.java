package com.microsaas.cacheoptimizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CacheOptimizerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CacheOptimizerApplication.class, args);
    }
}
