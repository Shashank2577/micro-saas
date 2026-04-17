package com.microsaas.brandvoice.controller;

import com.microsaas.brandvoice.service.PdfExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/content-audits")
@RequiredArgsConstructor
public class PdfExportController {

    private final PdfExportService pdfExportService;

    @GetMapping("/{auditId}/pdf")
    public ResponseEntity<byte[]> exportPdf(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID auditId) {

        byte[] pdfBytes = pdfExportService.exportAuditToPdf(tenantId, auditId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "audit-" + auditId + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
