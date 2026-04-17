package com.microsaas.compbenchmark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CompBenchmarkApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompBenchmarkApplication.class, args);
    }
}
