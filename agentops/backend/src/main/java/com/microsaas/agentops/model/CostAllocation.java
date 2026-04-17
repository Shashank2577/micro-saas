package com.microsaas.agentops.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cost_allocation")
@Data
public class CostAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "agent_id", nullable = false)
    private String agentId;

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "product_feature")
    private String productFeature;

    @Column
    private String period;

    @Column(name = "total_cost", nullable = false)
    private BigDecimal totalCost;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
