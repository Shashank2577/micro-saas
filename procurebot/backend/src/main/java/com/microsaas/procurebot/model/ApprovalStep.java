package com.microsaas.procurebot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "approval_steps")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalStep {

    @Id
    private UUID id;
    
    private UUID requestId;
    
    private String approverRole;
    
    private String approverEmail;
    
    @Enumerated(EnumType.STRING)
    private ApprovalStepStatus status;
    
    private ZonedDateTime decidedAt;
    
    @Column(columnDefinition = "TEXT")
    private String comments;
    
    private UUID tenantId;
}
