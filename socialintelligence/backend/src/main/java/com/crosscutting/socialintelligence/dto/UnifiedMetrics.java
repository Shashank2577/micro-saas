



package com.crosscutting.socialintelligence.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;





import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnifiedMetrics {
    private Long totalFollowers;
    private Long totalLikes;
    private Long totalComments;
    private Long totalShares;
    private Long totalViews;
    private BigDecimal averageEngagementRate;
}
