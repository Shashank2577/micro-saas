package com.microsaas.brandvoice.controller;

import com.microsaas.brandvoice.model.*;
import com.microsaas.brandvoice.service.BrandVoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandVoiceService brandVoiceService;

    public record CreateBrandProfileRequest(
            String name,
            Map<String, Object> toneAttributes,
            List<String> vocabularyApproved,
            List<String> vocabularyBanned,
            String styleGuide
    ) {}

    @PostMapping
    public ResponseEntity<BrandProfile> createBrandProfile(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody CreateBrandProfileRequest request) {
        return ResponseEntity.ok(brandVoiceService.createBrandProfile(
                tenantId,
                request.name(),
                request.toneAttributes(),
                request.vocabularyApproved(),
                request.vocabularyBanned(),
                request.styleGuide()
        ));
    }

    @GetMapping
    public ResponseEntity<List<BrandProfile>> listBrandProfiles(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(brandVoiceService.listBrandProfiles(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandProfile> getBrandProfile(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(brandVoiceService.getBrandProfile(id, tenantId));
    }

    public record AddCorpusRequest(String title, String content, boolean approved) {}

    @PostMapping("/{id}/corpus")
    public ResponseEntity<BrandCorpusItem> addCorpusItem(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id,
            @RequestBody AddCorpusRequest request) {
        return ResponseEntity.ok(brandVoiceService.addCorpusItem(
                id,
                tenantId,
                request.title(),
                request.content(),
                request.approved()
        ));
    }

    public record ReviewContentRequest(String content) {}

    @PostMapping("/{id}/review")
    public ResponseEntity<ContentReview> reviewContent(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id,
            @RequestBody ReviewContentRequest request) {
        return ResponseEntity.ok(brandVoiceService.reviewContent(
                id,
                tenantId,
                request.content()
        ));
    }

    @GetMapping("/{id}/trends")
    public ResponseEntity<List<ConsistencyTrend>> getTrends(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(brandVoiceService.getTrends(id, tenantId));
    }

    @GetMapping("/{id}/vocabulary")
    public ResponseEntity<Map<String, List<String>>> getVocabulary(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(brandVoiceService.getVocabulary(id, tenantId));
    }
}
