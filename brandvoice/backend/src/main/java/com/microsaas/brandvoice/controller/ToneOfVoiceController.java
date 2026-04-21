package com.microsaas.brandvoice.controller;

import com.microsaas.brandvoice.entity.ToneOfVoice;
import com.microsaas.brandvoice.service.ToneOfVoiceService;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/toneofvoices")
@RequiredArgsConstructor
public class ToneOfVoiceController {
    private final ToneOfVoiceService service;

    @GetMapping
    public List<ToneOfVoice> getAll() {
        return service.findAllByTenant(TenantContext.require());
    }

    @PostMapping
    public ToneOfVoice create(@RequestBody ToneOfVoice entity) {
        entity.setTenantId(TenantContext.require());
        return service.save(entity);
    }
}
