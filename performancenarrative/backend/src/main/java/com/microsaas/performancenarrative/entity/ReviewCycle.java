package com.microsaas.performancenarrative.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "review_cycle", schema = "performancenarrative")
@Data
public class ReviewCycle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String cycleName;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    
    @Enumerated(EnumType.STRING)
    private CycleStatus status;
    private UUID tenantId;

    public enum CycleStatus {
        DRAFT, ACTIVE, CLOSED
    }
}
