package com.microsaas.wealthplan.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "net_worth_snapshots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NetWorthSnapshot {
    @Id
    private UUID id = UUID.randomUUID();
    private String tenantId;
    private LocalDate snapshotDate;
    private BigDecimal totalAssets;
    private BigDecimal totalLiabilities;
    private BigDecimal netWorth;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
