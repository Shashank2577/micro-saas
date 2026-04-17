package com.microsaas.agentops.dto;

import lombok.Data;

@Data
public class AgentStepRequest {
    private String stepType;
    private String input;
    private String output;
    private Long durationMs;
}
