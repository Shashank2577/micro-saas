package com.microsaas.brandvoice.controller;

import com.microsaas.brandvoice.entity.ContentAsset;
import com.microsaas.brandvoice.service.ContentAssetService;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contentassets")
@RequiredArgsConstructor
public class ContentAssetController {
    private final ContentAssetService service;

    @GetMapping
    public List<ContentAsset> getAll() {
        return service.findAllByTenant(TenantContext.require());
    }

    @PostMapping
    public ContentAsset create(@RequestBody ContentAsset entity) {
        entity.setTenantId(TenantContext.require());
        return service.save(entity);
    }
}
