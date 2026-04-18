package com.microsaas.marketsignal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
    "com.microsaas.marketsignal",
    "com.crosscutting.starter"
})
public class MarketSignalApplication {
    public static void main(String[] args) {
        SpringApplication.run(MarketSignalApplication.class, args);
    }
}
