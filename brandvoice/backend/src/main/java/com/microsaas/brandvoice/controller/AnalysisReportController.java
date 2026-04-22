package com.microsaas.brandvoice.controller;

import com.microsaas.brandvoice.entity.AnalysisReport;
import com.microsaas.brandvoice.service.AnalysisReportService;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analysisreports")
@RequiredArgsConstructor
public class AnalysisReportController {
    private final AnalysisReportService service;

    @GetMapping
    public List<AnalysisReport> getAll() {
        return service.findAllByTenant(TenantContext.require());
    }

    @PostMapping
    public AnalysisReport create(@RequestBody AnalysisReport entity) {
        entity.setTenantId(TenantContext.require());
        return service.save(entity);
    }
}
