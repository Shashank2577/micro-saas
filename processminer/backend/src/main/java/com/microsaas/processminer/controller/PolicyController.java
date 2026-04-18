package com.microsaas.processminer.controller;

import com.microsaas.processminer.domain.Policy;
import com.microsaas.processminer.dto.PolicyCreateRequest;
import com.microsaas.processminer.repository.PolicyRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyRepository policyRepository;
    private final ObjectMapper objectMapper;

    public PolicyController(PolicyRepository policyRepository, ObjectMapper objectMapper) {
        this.policyRepository = policyRepository;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Policy create(@RequestBody @Valid PolicyCreateRequest request) {
        Policy policy = new Policy();
        policy.setTenantId(TenantContext.require());
        policy.setProcessModelId(request.processModelId());
        policy.setName(request.name());
        policy.setRuleDefinition(objectMapper.valueToTree(request.ruleDefinition()));
        return policyRepository.save(policy);
    }

    @GetMapping
    public List<Policy> getAll() {
        return policyRepository.findByTenantId(TenantContext.require());
    }
}
