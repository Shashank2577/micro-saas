package com.microsaas.localizationos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QualityMetrics {
    private int totalJobs;
    private int approvedJobs;
    private int pendingReviewJobs;
    private double averageConfidence;
    private int totalCulturalFlags;
}
