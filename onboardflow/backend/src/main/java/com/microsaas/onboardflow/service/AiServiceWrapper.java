package com.microsaas.onboardflow.service;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.Map;
@Service
public class AiServiceWrapper {
    public Map<String, Object> analyze(UUID planId, UUID tenantId) {
        return Map.of("insights", "Plan analytics mocked", "confidence", 0.95);
    }
}
