package com.microsaas.agentops.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AgentRunRequest {
    private String agentId;
    private String workflowId;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String status;
    private BigDecimal tokenCost;
}
