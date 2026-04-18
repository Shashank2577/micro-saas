package com.microsaas.dataunification.controller;

import com.microsaas.dataunification.model.SyncJob;
import com.microsaas.dataunification.service.SyncJobService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/jobs")
public class SyncJobController {
    private final SyncJobService service;

    public SyncJobController(SyncJobService service) {
        this.service = service;
    }

    @GetMapping
    public List<SyncJob> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public SyncJob getById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    public SyncJob create(@RequestBody SyncJob job) {
        return service.create(job);
    }

    @PostMapping("/{id}/rollback")
    public SyncJob rollback(@PathVariable UUID id) {
        return service.rollback(id);
    }
}
