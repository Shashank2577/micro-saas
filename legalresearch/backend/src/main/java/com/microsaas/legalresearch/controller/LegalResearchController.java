package com.microsaas.legalresearch.controller;

import com.microsaas.legalresearch.domain.Citation;
import com.microsaas.legalresearch.domain.CitationType;
import com.microsaas.legalresearch.domain.PracticeArea;
import com.microsaas.legalresearch.domain.ResearchMemo;
import com.microsaas.legalresearch.domain.ResearchThread;
import com.microsaas.legalresearch.service.ResearchService;
import com.microsaas.legalresearch.service.ThreadService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LegalResearchController {

    private final ResearchService researchService;
    private final ThreadService threadService;

    @PostMapping("/memos")
    public ResponseEntity<ResearchMemo> createMemo(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody CreateMemoRequest request) {
        return ResponseEntity.ok(researchService.createMemo(
                request.getTitle(), request.getQuery(), request.getPracticeArea(), tenantId));
    }

    @GetMapping("/memos")
    public ResponseEntity<List<ResearchMemo>> listMemos(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(researchService.listMemos(tenantId));
    }

    @GetMapping("/memos/search")
    public ResponseEntity<List<ResearchMemo>> searchMemos(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestParam("q") String query) {
        return ResponseEntity.ok(researchService.searchMemos(query, tenantId));
    }

    @PostMapping("/memos/{id}/citations")
    public ResponseEntity<Citation> addCitation(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable("id") UUID memoId,
            @RequestBody AddCitationRequest request) {
        return ResponseEntity.ok(researchService.addCitation(
                memoId, request.getType(), request.getReference(), request.getSummary(), tenantId));
    }

    @GetMapping("/memos/{id}/citations")
    public ResponseEntity<List<Citation>> listCitations(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable("id") UUID memoId) {
        return ResponseEntity.ok(researchService.listCitations(memoId, tenantId));
    }

    @PostMapping("/memos/{id}/complete")
    public ResponseEntity<ResearchMemo> completeMemo(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable("id") UUID memoId) {
        return ResponseEntity.ok(researchService.completeMemo(memoId, tenantId));
    }

    @PostMapping("/threads")
    public ResponseEntity<ResearchThread> createThread(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody CreateThreadRequest request) {
        return ResponseEntity.ok(threadService.createThread(
                request.getTitle(), request.getPracticeArea(), tenantId));
    }

    @GetMapping("/threads")
    public ResponseEntity<List<ResearchThread>> listThreads(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(threadService.listThreads(tenantId));
    }

    @PostMapping("/threads/{id}/memos/{memoId}")
    public ResponseEntity<Void> addMemoToThread(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable("id") UUID threadId,
            @PathVariable("memoId") UUID memoId) {
        threadService.addMemoToThread(threadId, memoId, tenantId);
        return ResponseEntity.ok().build();
    }

    @Data
    public static class CreateMemoRequest {
        private String title;
        private String query;
        private PracticeArea practiceArea;
    }

    @Data
    public static class AddCitationRequest {
        private CitationType type;
        private String reference;
        private String summary;
    }

    @Data
    public static class CreateThreadRequest {
        private String title;
        private PracticeArea practiceArea;
    }
}
