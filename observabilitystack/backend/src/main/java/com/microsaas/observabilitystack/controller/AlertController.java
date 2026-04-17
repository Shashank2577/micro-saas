package com.microsaas.observabilitystack.controller;

import com.microsaas.observabilitystack.entity.Alert;
import com.microsaas.observabilitystack.entity.AlertHistory;
import com.microsaas.observabilitystack.repository.AlertRepository;
import com.microsaas.observabilitystack.repository.AlertHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {
    private final AlertRepository repository;
    private final AlertHistoryRepository historyRepository;

    @PostMapping
    public Alert create(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody Alert entity) {
        entity.setTenantId(tenantId);
        return repository.save(entity);
    }

    @GetMapping
    public List<Alert> getAll(@RequestHeader("X-Tenant-ID") String tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @PutMapping("/{id}")
    public Alert update(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id, @RequestBody Alert entity) {
        Alert existing = repository.findByIdAndTenantId(id, tenantId).orElseThrow();
        existing.setStatus(entity.getStatus());
        existing.setResolvedAt(entity.getResolvedAt());
        existing.setUpdatedAt(LocalDateTime.now());

        AlertHistory history = new AlertHistory();
        history.setTenantId(tenantId);
        history.setAlert(existing);
        history.setStatus(entity.getStatus());
        historyRepository.save(history);

        return repository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        repository.delete(repository.findByIdAndTenantId(id, tenantId).orElseThrow());
    }

    @GetMapping("/{id}/history")
    public List<AlertHistory> getHistory(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        return historyRepository.findByTenantId(tenantId);
    }
}
