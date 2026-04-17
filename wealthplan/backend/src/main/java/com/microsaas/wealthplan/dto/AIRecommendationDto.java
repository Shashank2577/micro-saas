package com.microsaas.wealthplan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class AIRecommendationDto {
    private List<String> recommendations;
}
