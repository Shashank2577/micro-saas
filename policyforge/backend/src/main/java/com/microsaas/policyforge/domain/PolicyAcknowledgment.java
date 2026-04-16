package com.microsaas.policyforge.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "policy_acknowledgments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyAcknowledgment {
    @Id
    private UUID id;
    
    private UUID policyId;
    private String userId;
    private Instant acknowledgedAt;
    private UUID tenantId;
}
