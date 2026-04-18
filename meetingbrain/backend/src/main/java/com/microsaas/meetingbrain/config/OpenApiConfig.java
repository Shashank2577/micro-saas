package com.microsaas.meetingbrain.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI meetingBrainOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MeetingBrain API")
                        .description("API for MeetingBrain - AI Meeting Intelligence Platform")
                        .version("1.0"));
    }
}
