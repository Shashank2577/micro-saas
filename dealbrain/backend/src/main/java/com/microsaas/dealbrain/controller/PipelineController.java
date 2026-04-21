package com.microsaas.dealbrain.controller;

import com.microsaas.dealbrain.model.Deal;
import com.microsaas.dealbrain.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pipeline")
@RequiredArgsConstructor
public class PipelineController {

    private final DealRepository dealRepository;

    @GetMapping("/dashboard")
    public List<Deal> getPipelineDashboard(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return dealRepository.findByTenantId(tenantId);
    }
}
