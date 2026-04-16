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
@Table(name = "policy_versions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyVersion {
    @Id
    private UUID id;
    
    private UUID policyId;
    private int version;
    private String content;
    private String changedBy;
    private Instant changedAt;
    private String changeSummary;
    private UUID tenantId;
}
