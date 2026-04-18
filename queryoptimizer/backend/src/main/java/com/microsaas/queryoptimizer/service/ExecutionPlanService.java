package com.microsaas.queryoptimizer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class ExecutionPlanService {

    private final ObjectMapper objectMapper;

    public ExecutionPlanService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JsonNode parseExecutionPlan(String rawPlanJson) {
        try {
            if (rawPlanJson == null || rawPlanJson.isEmpty()) {
                return objectMapper.createObjectNode();
            }
            return objectMapper.readTree(rawPlanJson);
        } catch (Exception e) {
            System.err.println("Failed to parse execution plan: " + e.getMessage());
            return objectMapper.createObjectNode().put("error", "Invalid Plan Format");
        }
    }
}
