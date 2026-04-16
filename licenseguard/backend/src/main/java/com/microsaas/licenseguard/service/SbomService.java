package com.microsaas.licenseguard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.licenseguard.domain.Dependency;
import com.microsaas.licenseguard.domain.LicenseViolation;
import com.microsaas.licenseguard.domain.SbomReport;
import com.microsaas.licenseguard.repository.DependencyRepository;
import com.microsaas.licenseguard.repository.LicenseViolationRepository;
import com.microsaas.licenseguard.repository.SbomReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SbomService {

    private final DependencyRepository dependencyRepository;
    private final LicenseViolationRepository violationRepository;
    private final SbomReportRepository sbomReportRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public SbomReport generateSbom(UUID repositoryId, UUID tenantId) {
        List<Dependency> dependencies = dependencyRepository.findByRepositoryIdAndTenantId(repositoryId, tenantId);
        List<LicenseViolation> violations = violationRepository.findByRepositoryIdAndTenantId(repositoryId, tenantId);

        Map<String, Object> reportData = new HashMap<>();
        reportData.put("dependencies", dependencies);
        reportData.put("violations", violations);

        String reportJson = "";
        try {
            reportJson = objectMapper.writeValueAsString(reportData);
        } catch (JsonProcessingException e) {
            // handle or ignore
        }

        SbomReport report = SbomReport.builder()
                .id(UUID.randomUUID())
                .repositoryId(repositoryId)
                .tenantId(tenantId)
                .generatedAt(Instant.now())
                .dependencyCount(dependencies.size())
                .violationCount(violations.size())
                .reportJson(reportJson)
                .build();

        return sbomReportRepository.save(report);
    }
}
