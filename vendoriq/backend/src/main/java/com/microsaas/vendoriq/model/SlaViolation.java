package com.microsaas.vendoriq.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sla_violations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlaViolation {
    @Id
    private UUID id;
    
    private UUID vendorId;
    
    @Enumerated(EnumType.STRING)
    private ViolationType violationType;
    
    private String description;
    
    private LocalDateTime occurredAt;
    
    @Enumerated(EnumType.STRING)
    private Severity severity;
    
    @Enumerated(EnumType.STRING)
    private ViolationStatus status;
    
    private UUID tenantId;
}
