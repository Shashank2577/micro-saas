package com.microsaas.policyforge.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "policies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Policy {
    @Id
    private UUID id;
    
    private String title;
    private String content;
    private String department;
    private int version;
    
    @Enumerated(EnumType.STRING)
    private PolicyStatus status;
    
    private String owner;
    private UUID tenantId;
    private Instant createdAt;
    private Instant updatedAt;
}
