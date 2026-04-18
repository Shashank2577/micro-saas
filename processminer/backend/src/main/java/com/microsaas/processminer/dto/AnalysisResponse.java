package com.microsaas.processminer.dto;

import com.microsaas.processminer.domain.AnalysisResult;
import java.util.List;

public record AnalysisResponse(
    List<AnalysisResult> bottlenecks,
    List<AnalysisResult> complianceGaps,
    List<AnalysisResult> variants
) {}
