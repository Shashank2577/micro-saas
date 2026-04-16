package com.microsaas.nexushub.workflow;

import java.util.List;
import java.util.Map;

public record CreateWorkflowRequest(
        String name,
        String description,
        Map<String, Object> triggerCondition,
        List<Map<String, Object>> steps
) {}
