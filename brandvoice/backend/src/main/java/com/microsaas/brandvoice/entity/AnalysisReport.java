package com.microsaas.brandvoice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "analysis_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalysisReport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID tenantId;
    private UUID contentAssetId;
    private Double score;
    @Column(columnDefinition="TEXT")
    private String feedback;
    private LocalDateTime generatedAt;
}
