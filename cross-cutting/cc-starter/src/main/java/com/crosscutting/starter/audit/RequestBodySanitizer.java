package com.crosscutting.starter.audit;

import com.crosscutting.starter.CcProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class RequestBodySanitizer {

    private final List<String> sensitiveFields;
    private final ObjectMapper objectMapper;

    public RequestBodySanitizer(CcProperties properties, ObjectMapper objectMapper) {
        this.sensitiveFields = properties.getAudit().getSensitiveFields();
        this.objectMapper = objectMapper;
    }

    public String sanitize(String jsonBody) {
        if (jsonBody == null || jsonBody.isBlank()) {
            return null;
        }

        try {
            JsonNode root = objectMapper.readTree(jsonBody);
            sanitizeNode(root);
            return objectMapper.writeValueAsString(root);
        } catch (JsonProcessingException e) {
            return "{\"_raw\": \"[unparseable]\"}";
        }
    }

    private void sanitizeNode(JsonNode node) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                if (isSensitive(entry.getKey())) {
                    objectNode.put(entry.getKey(), "***");
                } else {
                    sanitizeNode(entry.getValue());
                }
            }
        } else if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            for (JsonNode element : arrayNode) {
                sanitizeNode(element);
            }
        }
    }

    private boolean isSensitive(String fieldName) {
        return sensitiveFields.stream()
                .anyMatch(sensitive -> sensitive.equalsIgnoreCase(fieldName));
    }
}
