package com.microsaas.insuranceai.controller;

import com.microsaas.insuranceai.domain.Policy;
import com.microsaas.insuranceai.service.PolicyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @GetMapping
    public List<Policy> getPolicies() {
        return policyService.getAllPolicies();
    }

    @GetMapping("/{id}")
    public Policy getPolicy(@PathVariable UUID id) {
        return policyService.getPolicy(id);
    }

    @PostMapping
    public Policy createPolicy(@RequestBody Policy policy) {
        return policyService.createPolicy(policy);
    }
}
