package com.crosscutting.socialintelligence.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "growth_recommendations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrowthRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID tenantId;
    private String recommendationText;
    private String platform;
    private String category;
    private Integer priority;
    @Builder.Default
    private LocalDateTime generatedAt = LocalDateTime.now();
    @Builder.Default
    private Boolean isActioned = false;
}
