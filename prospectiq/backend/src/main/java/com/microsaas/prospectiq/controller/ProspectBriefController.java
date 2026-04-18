package com.microsaas.prospectiq.controller;

import com.microsaas.prospectiq.model.ProspectBrief;
import com.microsaas.prospectiq.service.ProspectBriefService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Prospect Briefs")
public class ProspectBriefController {

    private final ProspectBriefService prospectBriefService;

    @PostMapping("/prospects/{id}/briefs")
    @Operation(summary = "Generate a new prospect brief")
    public ResponseEntity<ProspectBrief> generateBrief(@PathVariable UUID id) {
        return ResponseEntity.ok(prospectBriefService.generateBrief(id));
    }

    @GetMapping("/prospects/{id}/briefs")
    @Operation(summary = "Get the latest brief for a prospect")
    public ResponseEntity<ProspectBrief> getLatestBrief(@PathVariable UUID id) {
        return ResponseEntity.ok(prospectBriefService.getLatestBrief(id));
    }

    @GetMapping("/briefs/{id}/export/pdf")
    @Operation(summary = "Export brief as PDF")
    public ResponseEntity<byte[]> exportPdf(@PathVariable UUID id) {
        // Mock PDF export for now
        byte[] pdfBytes = "Mock PDF Content".getBytes();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"brief.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
