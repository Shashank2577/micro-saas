package com.microsaas.policyforge.controller;

import com.microsaas.policyforge.domain.Policy;
import com.microsaas.policyforge.domain.PolicyAcknowledgment;
import com.microsaas.policyforge.service.AcknowledgmentService;
import com.microsaas.policyforge.service.PolicyService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/policies")
@RequiredArgsConstructor
public class PolicyController {
    private final PolicyService policyService;
    private final AcknowledgmentService acknowledgmentService;

    @Data
    public static class CreatePolicyRequest {
        private String title;
        private String content;
        private String department;
        private String owner;
    }

    @Data
    public static class UpdatePolicyRequest {
        private String content;
        private String changedBy;
        private String changeSummary;
    }

    @Data
    public static class AcknowledgeRequest {
        private String userId;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Policy createPolicy(@RequestBody CreatePolicyRequest request,
                               @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return policyService.createPolicy(request.getTitle(), request.getContent(),
                request.getDepartment(), request.getOwner(), tenantId);
    }

    @GetMapping
    public List<Policy> listPolicies(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return policyService.listPolicies(tenantId);
    }

    @GetMapping("/{id}")
    public Policy getPolicy(@PathVariable UUID id,
                            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return policyService.getPolicy(id, tenantId);
    }

    @PutMapping("/{id}")
    public Policy updatePolicy(@PathVariable UUID id,
                               @RequestBody UpdatePolicyRequest request,
                               @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return policyService.updatePolicy(id, request.getContent(),
                request.getChangedBy(), request.getChangeSummary(), tenantId);
    }

    @PostMapping("/{id}/activate")
    public Policy activatePolicy(@PathVariable UUID id,
                                 @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return policyService.activatePolicy(id, tenantId);
    }

    @PostMapping("/{id}/acknowledge")
    @ResponseStatus(HttpStatus.CREATED)
    public PolicyAcknowledgment acknowledge(@PathVariable UUID id,
                                            @RequestBody AcknowledgeRequest request,
                                            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return acknowledgmentService.acknowledge(id, request.getUserId(), tenantId);
    }

    @GetMapping("/{id}/acknowledgments")
    public List<PolicyAcknowledgment> getAcknowledgments(@PathVariable UUID id,
                                                         @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return acknowledgmentService.getAcknowledgments(id, tenantId);
    }
}
