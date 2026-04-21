package com.microsaas.brandvoice.controller;

import com.microsaas.brandvoice.entity.VocabularyList;
import com.microsaas.brandvoice.service.VocabularyListService;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vocabularylists")
@RequiredArgsConstructor
public class VocabularyListController {
    private final VocabularyListService service;

    @GetMapping
    public List<VocabularyList> getAll() {
        return service.findAllByTenant(TenantContext.require());
    }

    @PostMapping
    public VocabularyList create(@RequestBody VocabularyList entity) {
        entity.setTenantId(TenantContext.require());
        return service.save(entity);
    }
}
