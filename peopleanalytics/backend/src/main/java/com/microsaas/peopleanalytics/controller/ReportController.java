package com.microsaas.peopleanalytics.controller;

import com.microsaas.peopleanalytics.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/people-analytics/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/export/pdf")
    @PreAuthorize("hasAnyRole('HR', 'EXECUTIVE')")
    public ResponseEntity<byte[]> exportPdf() {
        byte[] pdf = reportService.exportEmployeesToPdf();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employee_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/export/csv")
    @PreAuthorize("hasAnyRole('HR', 'EXECUTIVE')")
    public ResponseEntity<byte[]> exportCsv() {
        byte[] csv = reportService.exportEmployeesToCsv();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employee_report.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csv);
    }
}
