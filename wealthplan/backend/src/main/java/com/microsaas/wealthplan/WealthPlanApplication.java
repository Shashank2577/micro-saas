package com.microsaas.wealthplan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class WealthPlanApplication {
    public static void main(String[] args) {
        SpringApplication.run(WealthPlanApplication.class, args);
    }
}
