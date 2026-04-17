package com.crosscutting.starter;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(OpenAPI.class)
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI ccOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cross-Cutting Platform API")
                        .version("0.1.0")
                        .description("Unified API for cross-cutting SaaS concerns — "
                                + "tenancy, RBAC, audit, auth, notifications, storage, and more.")
                        .contact(new Contact()
                                .name("Cross-Cutting Platform")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT token obtained from the auth endpoint")));
    }

    @Bean
    public GroupedOpenApi ccApiGroup() {
        return GroupedOpenApi.builder()
                .group("cc-platform")
                .pathsToMatch("/cc/**")
                .build();
    }
}
