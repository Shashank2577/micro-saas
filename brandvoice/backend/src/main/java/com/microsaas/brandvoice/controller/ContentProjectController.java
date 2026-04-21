package com.microsaas.brandvoice.controller;

import com.microsaas.brandvoice.entity.ContentProject;
import com.microsaas.brandvoice.service.ContentProjectService;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contentprojects")
@RequiredArgsConstructor
public class ContentProjectController {
    private final ContentProjectService service;

    @GetMapping
    public List<ContentProject> getAll() {
        return service.findAllByTenant(TenantContext.require());
    }

    @PostMapping
    public ContentProject create(@RequestBody ContentProject entity) {
        entity.setTenantId(TenantContext.require());
        return service.save(entity);
    }
}
