package com.microsaas.marketsignal.controller;

import com.microsaas.marketsignal.domain.entity.MarketBrief;
import com.microsaas.marketsignal.service.BriefGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/briefs-generation")
@RequiredArgsConstructor
public class BriefGenerationController {

    private final BriefGenerationService briefGenerationService;

    @PostMapping("/generate")
    public ResponseEntity<MarketBrief> generateBrief() {
        MarketBrief brief = briefGenerationService.generateBrief();
        return ResponseEntity.ok(brief);
    }
}
