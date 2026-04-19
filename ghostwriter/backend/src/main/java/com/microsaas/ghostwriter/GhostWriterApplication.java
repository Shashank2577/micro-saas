package com.microsaas.ghostwriter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GhostWriterApplication {
    public static void main(String[] args) {
        SpringApplication.run(GhostWriterApplication.class, args);
    }
}
