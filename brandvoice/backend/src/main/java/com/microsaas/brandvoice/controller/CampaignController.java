package com.microsaas.brandvoice.controller;

import com.microsaas.brandvoice.entity.Campaign;
import com.microsaas.brandvoice.service.CampaignService;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
public class CampaignController {
    private final CampaignService service;

    @GetMapping
    public List<Campaign> getAll() {
        return service.findAllByTenant(TenantContext.require());
    }

    @PostMapping
    public Campaign create(@RequestBody Campaign entity) {
        entity.setTenantId(TenantContext.require());
        return service.save(entity);
    }
}
