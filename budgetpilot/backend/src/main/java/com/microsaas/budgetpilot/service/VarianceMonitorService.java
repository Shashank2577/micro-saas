package com.microsaas.budgetpilot.service;

import com.microsaas.budgetpilot.model.Variance;
import java.util.UUID;

public interface VarianceMonitorService {
    Variance calculateVariance(UUID budgetItemId);
    String generateVarianceExplanation(UUID varianceId);
}
