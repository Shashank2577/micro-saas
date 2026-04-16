package com.microsaas.auditready.domain;

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
@Table(name = "evidence_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvidenceItem {

    public enum SourceType {
        MANUAL, AUTOMATED
    }

    public enum Status {
        PENDING, ACCEPTED, REJECTED
    }

    @Id
    private UUID id;
    private UUID controlId;
    
    @Enumerated(EnumType.STRING)
    private SourceType sourceType;
    
    private String content;
    private Instant collectedAt;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    private UUID tenantId;
}
