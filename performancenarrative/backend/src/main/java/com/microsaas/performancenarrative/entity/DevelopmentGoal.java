package com.microsaas.performancenarrative.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "development_goal", schema = "performancenarrative")
@Data
public class DevelopmentGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID reviewId;
    
    @Column(columnDefinition = "TEXT")
    private String goalDescription;
    private LocalDate targetDate;
    
    @Enumerated(EnumType.STRING)
    private GoalStatus status;
    private UUID tenantId;

    public enum GoalStatus {
        PLANNED, IN_PROGRESS, ACHIEVED
    }
}
