package com.microsaas.apimanager.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SchemaDiffService {
    public Map<String, Object> compareSchemas(String oldSchema, String newSchema) {
        // Simplified mock for comparing schemas and detecting breaking changes
        boolean hasBreakingChanges = false;
        if (oldSchema != null && newSchema != null && oldSchema.length() > newSchema.length()) {
            hasBreakingChanges = true; // Simulating removed fields
        }

        return Map.of(
            "breakingChanges", hasBreakingChanges,
            "details", hasBreakingChanges ? List.of("Field removed in new version") : List.of()
        );
    }
}
