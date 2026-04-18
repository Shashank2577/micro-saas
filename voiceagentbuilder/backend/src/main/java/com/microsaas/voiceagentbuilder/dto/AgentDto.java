package com.microsaas.voiceagentbuilder.dto;

import lombok.Data;

@Data
public class AgentDto {
    private String name;
    private String description;
    private String systemPrompt;
    private String voiceId;
    private String language;
}
