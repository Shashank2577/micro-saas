package com.ecosystemmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.crosscutting.starter", "com.ecosystemmap"})
public class EcosystemMapApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcosystemMapApplication.class, args);
    }
}
