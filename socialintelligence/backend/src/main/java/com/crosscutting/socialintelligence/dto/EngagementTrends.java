



package com.crosscutting.socialintelligence.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;


import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EngagementTrends {
    private String period;
    private List<DailyTrend> trends;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
    public static class DailyTrend {
        private LocalDate date;
        private BigDecimal rate;
    }
}
