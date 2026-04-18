package com.microsaas.voiceagentbuilder.dto;

import lombok.Data;

@Data
public class AgentIntentDto {
    private String name;
    private String description;
    private String actionType;
    private String actionConfig;
}
