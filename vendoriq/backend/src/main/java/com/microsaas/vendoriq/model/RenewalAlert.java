package com.microsaas.vendoriq.model;

import jakarta.persistence.Entity;
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
@Table(name = "renewal_alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RenewalAlert {
    @Id
    private UUID id;
    
    private UUID vendorId;
    
    private LocalDate alertDate;
    
    private Integer daysUntilRenewal;
    
    private BigDecimal marketRateBenchmark;
    
    private BigDecimal currentRate;
    
    private UUID tenantId;
}
