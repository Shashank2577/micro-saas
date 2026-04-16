package com.microsaas.dataprivacyai.controller;

import com.microsaas.dataprivacyai.domain.DataFlow;
import com.microsaas.dataprivacyai.domain.DataSubjectRequest;
import com.microsaas.dataprivacyai.domain.PrivacyRisk;
import com.microsaas.dataprivacyai.repository.PrivacyRiskRepository;
import com.microsaas.dataprivacyai.service.DataFlowService;
import com.microsaas.dataprivacyai.service.DataSubjectRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PrivacyController {

    private final DataFlowService dataFlowService;
    private final DataSubjectRequestService dataSubjectRequestService;
    private final PrivacyRiskRepository privacyRiskRepository;

    @PostMapping("/data-flows")
    public ResponseEntity<DataFlow> addDataFlow(@RequestHeader("X-Tenant-ID") UUID tenantId,
                                                @RequestBody PrivacyDto.DataFlowRequest request) {
        DataFlow flow = dataFlowService.addDataFlow(
                request.sourceSystem,
                request.destinationSystem,
                request.dataCategory,
                request.transferMechanism,
                request.legalBasis,
                tenantId);
        return ResponseEntity.ok(flow);
    }

    @GetMapping("/data-flows")
    public ResponseEntity<List<DataFlow>> listDataFlows(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(dataFlowService.listDataFlows(tenantId));
    }

    @PostMapping("/data-flows/{id}/assess-risks")
    public ResponseEntity<Void> assessRisks(@RequestHeader("X-Tenant-ID") UUID tenantId,
                                            @PathVariable UUID id) {
        dataFlowService.assessRisks(id, tenantId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/risks")
    public ResponseEntity<List<PrivacyRisk>> listRisks(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(privacyRiskRepository.findByTenantId(tenantId));
    }

    @PostMapping("/subject-requests")
    public ResponseEntity<DataSubjectRequest> submitRequest(@RequestHeader("X-Tenant-ID") UUID tenantId,
                                                            @RequestBody PrivacyDto.SubjectRequest request) {
        DataSubjectRequest subjectRequest = dataSubjectRequestService.submitRequest(
                request.requestType,
                request.subjectEmail,
                tenantId);
        return ResponseEntity.ok(subjectRequest);
    }

    @GetMapping("/subject-requests")
    public ResponseEntity<List<DataSubjectRequest>> listPendingRequests(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(dataSubjectRequestService.listPendingRequests(tenantId));
    }

    @GetMapping("/subject-requests/overdue")
    public ResponseEntity<List<DataSubjectRequest>> listOverdueRequests(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(dataSubjectRequestService.listOverdueRequests(tenantId));
    }

    @PatchMapping("/subject-requests/{id}/start")
    public ResponseEntity<Void> startProcessing(@RequestHeader("X-Tenant-ID") UUID tenantId,
                                                @PathVariable UUID id) {
        dataSubjectRequestService.startProcessing(id, tenantId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/subject-requests/{id}/complete")
    public ResponseEntity<Void> completeRequest(@RequestHeader("X-Tenant-ID") UUID tenantId,
                                                @PathVariable UUID id) {
        dataSubjectRequestService.completeRequest(id, tenantId);
        return ResponseEntity.ok().build();
    }
}
