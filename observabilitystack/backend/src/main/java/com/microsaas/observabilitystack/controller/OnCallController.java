package com.microsaas.observabilitystack.controller;

import com.microsaas.observabilitystack.entity.OnCallSchedule;
import com.microsaas.observabilitystack.repository.OnCallScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/oncall")
@RequiredArgsConstructor
public class OnCallController {
    private final OnCallScheduleRepository repository;

    @PostMapping("/schedules")
    public OnCallSchedule create(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody OnCallSchedule entity) {
        entity.setTenantId(tenantId);
        return repository.save(entity);
    }

    @GetMapping("/schedules")
    public List<OnCallSchedule> getAll(@RequestHeader("X-Tenant-ID") String tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @GetMapping("/current-on-call")
    public OnCallSchedule getCurrent(@RequestHeader("X-Tenant-ID") String tenantId) {
        return repository.findByTenantId(tenantId).stream()
            .filter(s -> s.getStartTime().isBefore(LocalDateTime.now()) && s.getEndTime().isAfter(LocalDateTime.now()))
            .findFirst()
            .orElse(null);
    }

    @PostMapping("/escalate")
    public void escalate(@RequestHeader("X-Tenant-ID") String tenantId) {
        // Implementation for escalating on-call
    }
}
