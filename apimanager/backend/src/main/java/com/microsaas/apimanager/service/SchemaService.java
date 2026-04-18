package com.microsaas.apimanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SchemaService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> parseSchema(String schemaContent) {
        SwaggerParseResult result = new OpenAPIV3Parser().readContents(schemaContent, null, null);
        if (result.getMessages() != null && !result.getMessages().isEmpty() && result.getOpenAPI() == null) {
            throw new IllegalArgumentException("Invalid schema: " + String.join(", ", result.getMessages()));
        }
        
        OpenAPI openAPI = result.getOpenAPI();
        return objectMapper.convertValue(openAPI, Map.class);
    }
}
