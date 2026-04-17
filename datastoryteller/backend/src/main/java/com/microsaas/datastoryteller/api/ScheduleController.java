package com.microsaas.datastoryteller.api;

import com.microsaas.datastoryteller.domain.model.ScheduledDelivery;
import com.microsaas.datastoryteller.service.NarrativeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedules", description = "Scheduled Deliveries")
public class ScheduleController {

    private final NarrativeService narrativeService;

    @GetMapping
    @Operation(summary = "List schedules")
    public ResponseEntity<List<ScheduledDelivery>> listSchedules(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(narrativeService.listSchedules(tenantId));
    }

    @PostMapping
    @Operation(summary = "Register cron schedule")
    public ResponseEntity<ScheduledDelivery> createSchedule(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody ScheduledDelivery delivery) {
        delivery.setTenantId(tenantId);
        return ResponseEntity.ok(narrativeService.createSchedule(delivery));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete schedule")
    public ResponseEntity<Void> deleteSchedule(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        narrativeService.deleteSchedule(id, tenantId);
        return ResponseEntity.noContent().build();
    }
}
