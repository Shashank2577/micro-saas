package com.microsaas.meetingbrain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MeetingBrainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MeetingBrainApplication.class, args);
    }
}
