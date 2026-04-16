package com.microsaas.contractportfolio.controller;

import com.microsaas.contractportfolio.domain.ContractRecord;
import com.microsaas.contractportfolio.domain.ExtractedTerm;
import com.microsaas.contractportfolio.domain.RenewalAlert;
import com.microsaas.contractportfolio.service.ContractRecordService;
import com.microsaas.contractportfolio.service.RenewalAlertService;
import com.microsaas.contractportfolio.service.TermExtractionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ContractPortfolioController {

    private final ContractRecordService contractRecordService;
    private final TermExtractionService termExtractionService;
    private final RenewalAlertService renewalAlertService;

    @PostMapping("/contracts")
    public ResponseEntity<ContractRecord> createContract(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody CreateContractRequest request) {
        ContractRecord contract = contractRecordService.ingestContract(
                request.getCounterparty(),
                request.getContractType(),
                request.getStartDate(),
                request.getEndDate(),
                request.isAutoRenews(),
                request.getRenewalNoticeDays(),
                tenantId
        );
        return ResponseEntity.ok(contract);
    }

    @GetMapping("/contracts")
    public ResponseEntity<List<ContractRecord>> listContracts(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(contractRecordService.listContracts(tenantId));
    }

    @GetMapping("/contracts/expiring")
    public ResponseEntity<List<ContractRecord>> getExpiringContracts(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestParam(defaultValue = "90") int days) {
        return ResponseEntity.ok(contractRecordService.getExpiringContracts(tenantId, days));
    }

    @PostMapping("/contracts/{id}/extract-terms")
    public ResponseEntity<List<ExtractedTerm>> extractTerms(
            @PathVariable UUID id,
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody ExtractTermsRequest request) {
        return ResponseEntity.ok(termExtractionService.extractTerms(id, request.getRawContent(), tenantId));
    }

    @GetMapping("/contracts/{id}/terms")
    public ResponseEntity<List<ExtractedTerm>> getTerms(
            @PathVariable UUID id,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(termExtractionService.getTerms(id, tenantId));
    }

    @PostMapping("/contracts/{id}/generate-alerts")
    public ResponseEntity<List<RenewalAlert>> generateAlerts(
            @PathVariable UUID id,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(renewalAlertService.generateRenewalAlerts(id, tenantId));
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<RenewalAlert>> listAlerts(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(renewalAlertService.listAlerts(tenantId));
    }

    @PostMapping("/alerts/{id}/acknowledge")
    public ResponseEntity<RenewalAlert> acknowledgeAlert(
            @PathVariable UUID id,
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody AcknowledgeAlertRequest request) {
        return ResponseEntity.ok(renewalAlertService.acknowledgeAlert(id, request.getAcknowledgedBy(), tenantId));
    }

    @Data
    public static class CreateContractRequest {
        private String counterparty;
        private String contractType;
        private LocalDate startDate;
        private LocalDate endDate;
        private boolean autoRenews;
        private int renewalNoticeDays;
    }

    @Data
    public static class ExtractTermsRequest {
        private String rawContent;
    }

    @Data
    public static class AcknowledgeAlertRequest {
        private String acknowledgedBy;
    }
}
