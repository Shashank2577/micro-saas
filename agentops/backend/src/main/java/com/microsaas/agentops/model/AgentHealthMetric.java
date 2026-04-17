package com.microsaas.agentops.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "agent_health_metric")
@Data
public class AgentHealthMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "agent_id", nullable = false)
    private String agentId;

    @Column(name = "period_start", nullable = false)
    private LocalDateTime periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDateTime periodEnd;

    @Column(name = "success_rate")
    private BigDecimal successRate;

    @Column(name = "avg_cost")
    private BigDecimal avgCost;

    @Column(name = "avg_latency_ms")
    private Long avgLatencyMs;

    @Column(name = "escalation_rate")
    private BigDecimal escalationRate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
