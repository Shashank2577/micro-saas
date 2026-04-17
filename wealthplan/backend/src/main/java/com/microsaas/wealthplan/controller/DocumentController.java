package com.microsaas.wealthplan.controller;

import com.microsaas.wealthplan.entity.EstateDocument;
import com.microsaas.wealthplan.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @GetMapping("/plan-pdf")
    public ResponseEntity<byte[]> generatePlanPdf() {
        byte[] pdf = documentService.generatePlanPdf();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"financial-plan.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/estate-checklist")
    public ResponseEntity<List<EstateDocument>> downloadEstateChecklist() {
        return ResponseEntity.ok(documentService.getEstateChecklist());
    }

    @GetMapping("/export-goals")
    public ResponseEntity<String> exportGoals() {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"goals.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(documentService.exportGoalsCsv());
    }
}
