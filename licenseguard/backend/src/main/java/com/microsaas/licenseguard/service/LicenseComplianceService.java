package com.microsaas.licenseguard.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.ai.ChatRequest;
import com.microsaas.licenseguard.domain.*;
import com.microsaas.licenseguard.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LicenseComplianceService {

    private final AiService aiService;
    private final RepositoryRepository repositoryRepository;
    private final DependencyRepository dependencyRepository;
    private final CompliancePolicyRepository policyRepository;
    private final LicenseViolationRepository violationRepository;
    private final ScanJobRepository scanJobRepository;
    private final SbomReportRepository sbomReportRepository;

    @Async
    public void analyzeRepository(UUID tenantId, UUID repositoryId) {
        ScanJob scanJob = ScanJob.builder()
                .id(UUID.randomUUID())
                .repositoryId(repositoryId)
                .tenantId(tenantId)
                .status("IN_PROGRESS")
                .startedAt(LocalDateTime.now())
                .build();
        scanJobRepository.save(scanJob);

        List<Dependency> dependencies = dependencyRepository.findByRepositoryIdAndTenantId(repositoryId, tenantId);

        List<CompliancePolicy> policies = policyRepository.findByTenantId(tenantId);
        int violationCount = 0;

        for (Dependency dep : dependencies) {
            // Simulated RAG: Retrieve context from a vector database (mocked for this spec)
            String retrievedRagContext = "License Obligation Database Context for " + dep.getLicense() + ": " +
                    "Obligations include providing source code and attributing the author. " +
                    "Incompatible with proprietary software distribution.";

            String policiesDescription = policies.stream()
                .map(p -> p.getName() + ": Allowed -> " + p.getAllowedLicensesJson() + ", Denied -> " + p.getDeniedLicensesJson())
                .collect(Collectors.joining("; "));

            String prompt = String.format("Analyze dependency '%s' version '%s' with license '%s'. " +
                    "Retrieved RAG Context: %s. " +
                    "Policies: %s. " +
                    "Is this license compliant with the policies? Answer 'COMPLIANT' or 'VIOLATION' and provide a short reason.",
                    dep.getName(), dep.getVersion(), dep.getLicense(), retrievedRagContext, policiesDescription);

            String response = aiService.chat(new ChatRequest("gpt-4", List.of(new ChatMessage("user", prompt)), null, null)).content();

            if (response.contains("VIOLATION")) {
                violationCount++;
                LicenseViolation violation = LicenseViolation.builder()
                        .id(UUID.randomUUID())
                        .repositoryId(repositoryId)
                        .dependencyId(dep.getId())
                        .violationType("LICENSE_NOT_ALLOWED")
                        .description(response)
                        .severity("HIGH")
                        .status("OPEN")
                        .tenantId(tenantId)
                        .build();
                violationRepository.save(violation);
            }
        }

        SbomReport report = SbomReport.builder()
                .id(UUID.randomUUID())
                .repositoryId(repositoryId)
                .generatedAt(java.time.Instant.now())
                .dependencyCount(dependencies.size())
                .violationCount(violationCount)
                .reportJson("{ \"status\": \"generated\" }")
                .tenantId(tenantId)
                .build();
        sbomReportRepository.save(report);

        scanJob.setStatus("COMPLETED");
        scanJob.setCompletedAt(LocalDateTime.now());
        scanJobRepository.save(scanJob);
    }
}
