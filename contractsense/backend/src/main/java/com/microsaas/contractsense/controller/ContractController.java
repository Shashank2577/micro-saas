package com.microsaas.contractsense.controller;

import com.microsaas.contractsense.domain.Contract;
import com.microsaas.contractsense.domain.RiskAssessment;
import com.microsaas.contractsense.service.ContractService;
import com.microsaas.contractsense.service.RiskAnalysisService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;
    private final RiskAnalysisService riskAnalysisService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contract uploadContract(
            @RequestBody UploadRequest request,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return contractService.uploadContract(
                request.getTitle(),
                request.getCounterparty(),
                request.getContractType(),
                request.getContent(),
                tenantId
        );
    }

    @GetMapping
    public List<Contract> listContracts(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return contractService.listContracts(tenantId);
    }

    @GetMapping("/{id}")
    public Contract getContract(
            @PathVariable UUID id,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return contractService.getContract(id, tenantId);
    }

    @PostMapping("/{id}/analyze")
    public RiskAssessment analyzeContract(
            @PathVariable UUID id,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return contractService.analyzeContract(id, tenantId);
    }

    @GetMapping("/{id}/risk")
    public RiskAssessment getRiskAssessment(
            @PathVariable UUID id,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        // Just return the analysis results, maybe triggering a new one if not exists
        return contractService.analyzeContract(id, tenantId);
    }

    @Data
    public static class UploadRequest {
        private String title;
        private String counterparty;
        private String contractType;
        private String content;
    }
}
