package com.microsaas.peopleanalytics.controller;

import com.microsaas.peopleanalytics.model.TeamHealthMetric;
import com.microsaas.peopleanalytics.service.ReportService;
import com.microsaas.peopleanalytics.service.TeamHealthAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/people-analytics/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final TeamHealthAnalysisService teamHealthAnalysisService;

    @GetMapping("/team-health")
    public ResponseEntity<List<TeamHealthMetric>> getTeamHealth(@RequestParam(required = false) String department) {
        return ResponseEntity.ok(teamHealthAnalysisService.getTeamHealthByDepartment(department));
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPdf() {
        byte[] pdf = reportService.exportEmployeesToPdf();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employee_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportCsv() {
        byte[] csv = reportService.exportEmployeesToCsv();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employee_report.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csv);
    }
}
