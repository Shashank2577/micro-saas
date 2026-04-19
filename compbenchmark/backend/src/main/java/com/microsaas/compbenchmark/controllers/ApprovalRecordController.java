package com.microsaas.compbenchmark.controllers;

import com.microsaas.compbenchmark.model.ApprovalRecord;
import com.microsaas.compbenchmark.services.ApprovalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/compensation-benchmark/approval-records")
@RequiredArgsConstructor
public class ApprovalRecordController {
    private final ApprovalRecordService service;

    @GetMapping
    public List<ApprovalRecord> list() { return service.list(); }

    @PostMapping
    public ApprovalRecord create(@RequestBody ApprovalRecord entity) { return service.create(entity); }

    @GetMapping("/{id}")
    public ApprovalRecord getById(@PathVariable UUID id) { return service.getById(id); }

    @PatchMapping("/{id}")
    public ApprovalRecord update(@PathVariable UUID id, @RequestBody ApprovalRecord entity) { return service.update(id, entity); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) { service.delete(id); }

    @PostMapping("/{id}/validate")
    public Map<String, Object> validate(@PathVariable UUID id) { return service.validate(id); }

    @PostMapping("/{id}/simulate")
    public Map<String, Object> simulate(@PathVariable UUID id) { return service.simulate(id); }
}
