package com.microsaas.datacatalogai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.crosscutting", "com.microsaas.datacatalogai"})
public class DataCatalogAIApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataCatalogAIApplication.class, args);
    }
}
