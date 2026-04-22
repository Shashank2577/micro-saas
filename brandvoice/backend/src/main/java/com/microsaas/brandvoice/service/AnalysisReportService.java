package com.microsaas.brandvoice.service;

import com.microsaas.brandvoice.entity.AnalysisReport;
import com.microsaas.brandvoice.repository.AnalysisReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnalysisReportService {
    private final AnalysisReportRepository repository;

    public List<AnalysisReport> findAllByTenant(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public AnalysisReport save(AnalysisReport entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
