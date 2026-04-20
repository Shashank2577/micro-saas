package com.microsaas.onboardflow.dto;

import lombok.Data;
import java.util.Map;

@Data
public class OnboardingTaskRequest {
    private String name;
    private String status;
    private Map<String, Object> metadataJson;
}
