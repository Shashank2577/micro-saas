package com.microsaas.vendormonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VendorMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(VendorMonitorApplication.class, args);
    }
}
