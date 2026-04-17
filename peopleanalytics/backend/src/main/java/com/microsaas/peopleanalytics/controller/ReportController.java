package com.microsaas.peopleanalytics.controller;

import com.microsaas.peopleanalytics.service.InsightsGenerationService;
import com.microsaas.peopleanalytics.repository.EmployeeRepository;
import com.microsaas.peopleanalytics.model.Employee;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.UUID;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.StringWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final InsightsGenerationService insightsService;
    private final EmployeeRepository employeeRepository;

    @GetMapping("/predictive-insights")
    public ResponseEntity<Map<String, String>> getPredictiveInsights(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        String insight = insightsService.generatePredictiveInsights(tenantId);
        Map<String, String> response = new HashMap<>();
        response.put("insights", insight);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/export/csv", produces = "text/csv")
    public ResponseEntity<String> exportCsv(@RequestHeader("X-Tenant-ID") UUID tenantId) throws Exception {
        List<Employee> employees = employeeRepository.findByTenantId(tenantId);
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT.withHeader("Employee Number", "Name", "Department", "Role"));

        for (Employee e : employees) {
            printer.printRecord(e.getEmployeeNumber(), e.getFirstName() + " " + e.getLastName(), e.getDepartment(), e.getRole());
        }
        printer.flush();
        return ResponseEntity.ok(sw.toString());
    }

    @GetMapping(value = "/export/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportPdf(@RequestHeader("X-Tenant-ID") UUID tenantId) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();
        document.add(new Paragraph("PeopleAnalytics Report - Tenant: " + tenantId.toString()));
        document.add(new Paragraph("Predictive Insights: " + insightsService.generatePredictiveInsights(tenantId)));
        document.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
                .body(out.toByteArray());
    }
}
