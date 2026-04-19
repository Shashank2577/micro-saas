package com.microsaas.cashflowai.controller;

import com.microsaas.cashflowai.model.FundingEvent;
import com.microsaas.cashflowai.service.FundingEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cash-position/funding-events")
@RequiredArgsConstructor
public class FundingEventController {
    private final FundingEventService service;

    @GetMapping
    public ResponseEntity<List<FundingEvent>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FundingEvent> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<FundingEvent> create(@RequestBody FundingEvent entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FundingEvent> update(@PathVariable UUID id, @RequestBody FundingEvent entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id) {
        service.validate(id);
        return ResponseEntity.ok().build();
    }
}
