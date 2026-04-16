package com.microsaas.equityintelligence.controllers;

import com.microsaas.equityintelligence.model.FundingRound;
import com.microsaas.equityintelligence.services.FundingRoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/funding-rounds")
@RequiredArgsConstructor
public class FundingRoundController {

    private final FundingRoundService fundingRoundService;

    @PostMapping
    public ResponseEntity<FundingRound> createFundingRound(
            @RequestBody FundingRound fundingRound,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(fundingRoundService.createFundingRound(fundingRound, tenantId));
    }
}
