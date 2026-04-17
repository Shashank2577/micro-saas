package com.microsaas.wealthplan.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "allocation_recommendations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllocationRecommendation {
    @Id
    private UUID id = UUID.randomUUID();
    private String tenantId;
    private int age;
    private String riskTolerance;
    private int stocksPercentage;
    private int bondsPercentage;
    private int cashPercentage;
    private String recommendationText;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
