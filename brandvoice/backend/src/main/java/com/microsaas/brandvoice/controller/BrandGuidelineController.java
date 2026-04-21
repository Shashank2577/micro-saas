package com.microsaas.brandvoice.controller;

import com.microsaas.brandvoice.entity.BrandGuideline;
import com.microsaas.brandvoice.service.BrandGuidelineService;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brandguidelines")
@RequiredArgsConstructor
public class BrandGuidelineController {
    private final BrandGuidelineService service;

    @GetMapping
    public List<BrandGuideline> getAll() {
        return service.findAllByTenant(TenantContext.require());
    }

    @PostMapping
    public BrandGuideline create(@RequestBody BrandGuideline entity) {
        entity.setTenantId(TenantContext.require());
        return service.save(entity);
    }
}
