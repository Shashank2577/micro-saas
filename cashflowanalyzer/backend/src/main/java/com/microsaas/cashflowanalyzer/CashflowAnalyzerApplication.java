package com.microsaas.cashflowanalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.crosscutting.starter",
    "com.microsaas.cashflowanalyzer"
})
public class CashflowAnalyzerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CashflowAnalyzerApplication.class, args);
    }
}
