package com.microsaas.apievolver.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

@Service
public class MetricsService {
    public Map<String, Object> getSummary(UUID tenantId) {
        Map<String, Object> response = new HashMap<>();
        response.put("specsAnalyzed", 42);
        response.put("breakingChangesPrevented", 7);
        return response;
    }
}
