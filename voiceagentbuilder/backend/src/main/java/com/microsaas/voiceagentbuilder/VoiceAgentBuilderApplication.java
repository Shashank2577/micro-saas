package com.microsaas.voiceagentbuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VoiceAgentBuilderApplication {
    public static void main(String[] args) {
        SpringApplication.run(VoiceAgentBuilderApplication.class, args);
    }
}
