package com.microsaas.creatoranalytics.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "performance_model")
public class PerformanceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private UUID channelId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contentType;

    @Column(nullable = false)
    private Integer predictedViews;

    @Column(nullable = false)
    private Integer predictedConversions;

    @Column(nullable = false)
    private BigDecimal confidenceScore;

    @Column(nullable = false)
    private LocalDateTime modeledAt;
}
