package com.microsaas.billingai.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "BillingAI API", version = "v1", description = "BillingAI Production Progression Spec endpoints"))
public class OpenApiConfig {
}
