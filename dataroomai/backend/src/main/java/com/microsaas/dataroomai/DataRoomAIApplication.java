package com.microsaas.dataroomai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DataRoomAIApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataRoomAIApplication.class, args);
    }
}
