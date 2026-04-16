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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "vendors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendor {
    @Id
    private UUID id;
    
    private String name;
    private String category;
    private String contactEmail;
    private LocalDate contractEndDate;
    private BigDecimal monthlySpend;
    private Double slaUptime;
    private Integer healthScore;
    
    @Enumerated(EnumType.STRING)
    private VendorStatus status;
    
    private UUID tenantId;
}
