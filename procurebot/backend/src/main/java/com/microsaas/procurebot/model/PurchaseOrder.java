package com.microsaas.procurebot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "purchase_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrder {

    @Id
    private UUID id;
    
    private UUID requestId;
    
    private String vendorName;
    
    private BigDecimal amount;
    
    private ZonedDateTime issuedAt;
    
    private LocalDate expectedDelivery;
    
    @Enumerated(EnumType.STRING)
    private PurchaseOrderStatus status;
    
    private UUID tenantId;
}
