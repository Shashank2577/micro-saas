package com.microsaas.videonarrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VideoNarratorApplication {
    public static void main(String[] args) {
        SpringApplication.run(VideoNarratorApplication.class, args);
    }
}
