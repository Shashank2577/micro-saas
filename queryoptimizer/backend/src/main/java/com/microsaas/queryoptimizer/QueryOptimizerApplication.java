package com.microsaas.queryoptimizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.microsaas.queryoptimizer",
        "com.crosscutting.starter"
})
public class QueryOptimizerApplication {
    public static void main(String[] args) {
        SpringApplication.run(QueryOptimizerApplication.class, args);
    }
}
