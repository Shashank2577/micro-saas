package com.microsaas.notificationhub.api.dto;

import lombok.Data;
import java.util.Map;

@Data
public class TestTemplateRequest {
    private String testUserId;
    private Map<String, String> variables;
}
