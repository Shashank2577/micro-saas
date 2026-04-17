package com.microsaas.agentops.dto;

import lombok.Data;

@Data
public class EscalationRequest {
    private String reason;
    private String context;
}
