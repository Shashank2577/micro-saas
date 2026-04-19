package com.microsaas.performancenarrative.controller;

import com.microsaas.performancenarrative.entity.NarrativeDraft;
import com.microsaas.performancenarrative.service.NarrativeDraftService;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/v1/performance/narrative-drafts")
public class NarrativeDraftController {

    private final NarrativeDraftService service;

    public NarrativeDraftController(NarrativeDraftService service) {
        this.service = service;
    }

    @GetMapping
    public List<NarrativeDraft> list() {
        return service.list();
    }

    @PostMapping
    public NarrativeDraft create(@RequestBody NarrativeDraft entity) {
        return service.create(entity);
    }

    @GetMapping("/{id}")
    public NarrativeDraft getById(@PathVariable UUID id) {
        return service.getById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    @PatchMapping("/{id}")
    public NarrativeDraft update(@PathVariable UUID id, @RequestBody NarrativeDraft details) {
        return service.update(id, details);
    }

    @PostMapping("/{id}/validate")
    public boolean validate(@PathVariable UUID id) {
        return service.validate(id);
    }
}
