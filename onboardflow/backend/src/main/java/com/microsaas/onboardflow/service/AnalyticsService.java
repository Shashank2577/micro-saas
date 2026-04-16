package com.microsaas.onboardflow.service;

import com.microsaas.onboardflow.repository.ProductivityScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final ProductivityScoreRepository productivityScoreRepository;

    public Double getAverageTimeToProductivity() {
        Double avg = productivityScoreRepository.getAverageProductivityScore();
        return avg != null ? avg : 0.0;
    }
}
