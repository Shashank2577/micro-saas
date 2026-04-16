package com.microsaas.procurebot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "purchase_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseRequest {

    @Id
    private UUID id;

    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    private String requestedBy;
    
    private BigDecimal amount;
    
    private String category;
    
    @Enumerated(EnumType.STRING)
    private PurchaseRequestStatus status;
    
    private UUID tenantId;
    
    private ZonedDateTime createdAt;
}
