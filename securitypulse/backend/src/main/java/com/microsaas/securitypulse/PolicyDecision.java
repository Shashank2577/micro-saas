package com.microsaas.securitypulse;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "policy_decisions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyDecision {
    @Id
    private UUID id;
    private UUID scanJobId;
    private String decision;
    private String reason;
    private UUID tenantId;
}
