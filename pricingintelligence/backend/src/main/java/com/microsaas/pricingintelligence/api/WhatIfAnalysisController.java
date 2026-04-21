package com.microsaas.pricingintelligence.api;

import com.microsaas.pricingintelligence.service.WhatIfAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/what-if")
@RequiredArgsConstructor
public class WhatIfAnalysisController {

    private final WhatIfAnalysisService whatIfAnalysisService;

    @GetMapping("/simulate-revenue")
    public BigDecimal simulateRevenue(@RequestParam UUID segmentId, @RequestParam BigDecimal newPrice) {
        return whatIfAnalysisService.simulateRevenue(segmentId, newPrice);
    }
}
