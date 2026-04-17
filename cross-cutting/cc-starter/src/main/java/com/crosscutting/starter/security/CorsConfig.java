package com.crosscutting.starter.security;

import com.crosscutting.starter.CcProperties;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CorsConfig implements WebMvcConfigurer {

    private final CcProperties ccProperties;

    public CorsConfig(CcProperties ccProperties) {
        this.ccProperties = ccProperties;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] origins = ccProperties.getSecurity().getCorsOrigins()
                .toArray(new String[0]);

        registry.addMapping("/**")
                .allowedOrigins(origins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
