package com.microsaas.marketsignal.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.marketsignal.domain.entity.*;
import com.microsaas.marketsignal.dto.*;
import com.microsaas.marketsignal.repository.*;
import com.microsaas.marketsignal.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MarketSignalController {

    private final MarketSegmentRepository segmentRepository;
    private final InformationSourceRepository sourceRepository;
    private final MarketSignalRepository signalRepository;
    private final MarketPatternRepository patternRepository;
    private final MarketBriefRepository briefRepository;

    private final IngestionService ingestionService;
    private final AnalysisService analysisService;
    private final BriefGenerationService briefService;

    // --- Segments ---
    @GetMapping("/segments")
    public List<MarketSegment> getSegments() {
        return segmentRepository.findByTenantId(UUID.fromString(TenantContext.require().toString()));
    }

    @PostMapping("/segments")
    public MarketSegment createSegment(@RequestBody CreateSegmentRequest request) {
        MarketSegment segment = MarketSegment.builder()
                .tenantId(UUID.fromString(TenantContext.require().toString()))
                .name(request.getName())
                .description(request.getDescription())
                .keywords(request.getKeywords())
                .build();
        return segmentRepository.save(segment);
    }

    // --- Sources ---
    @GetMapping("/sources")
    public List<InformationSource> getSources() {
        return sourceRepository.findByTenantId(UUID.fromString(TenantContext.require().toString()));
    }

    @PostMapping("/sources")
    public InformationSource createSource(@RequestBody CreateSourceRequest request) {
        InformationSource source = InformationSource.builder()
                .tenantId(UUID.fromString(TenantContext.require().toString()))
                .name(request.getName())
                .sourceType(request.getSourceType())
                .url(request.getUrl())
                .active(true)
                .build();
        return sourceRepository.save(source);
    }

    // --- Signals ---
    @GetMapping("/signals")
    public List<MarketSignal> getSignals() {
        return signalRepository.findByTenantIdOrderByPublishedAtDesc(UUID.fromString(TenantContext.require().toString()));
    }

    @PostMapping("/signals")
    public MarketSignal ingestSignal(@RequestBody IngestSignalRequest request) {
        return ingestionService.ingestSignal(request);
    }

    // --- Patterns ---
    @GetMapping("/patterns")
    public List<MarketPattern> getPatterns() {
        return patternRepository.findByTenantIdOrderByDetectedAtDesc(UUID.fromString(TenantContext.require().toString()));
    }

    @PostMapping("/patterns/detect")
    public MarketPattern detectPatterns() {
        return analysisService.detectPatterns();
    }

    // --- Briefs ---
    @GetMapping("/briefs")
    public List<MarketBrief> getBriefs() {
        return briefRepository.findByTenantIdOrderByGeneratedAtDesc(UUID.fromString(TenantContext.require().toString()));
    }

    @PostMapping("/briefs/generate")
    public MarketBrief generateBrief() {
        return briefService.generateBrief();
    }
}
