package com.microsaas.debtnavigator.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.debtnavigator.entity.MilestoneTrack;
import com.microsaas.debtnavigator.service.MilestonesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/debt/milestone-tracks")
@RequiredArgsConstructor
public class MilestonesController {
    private final MilestonesService service;

    @GetMapping
    public List<MilestoneTrack> list() {
        return service.list(TenantContext.require());
    }

    @PostMapping
    public MilestoneTrack create(@RequestBody MilestoneTrack track) {
        track.setTenantId(TenantContext.require());
        return service.create(track);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MilestoneTrack> get(@PathVariable UUID id) {
        return service.getById(id, TenantContext.require())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MilestoneTrack> update(@PathVariable UUID id, @RequestBody MilestoneTrack track) {
        return service.getById(id, TenantContext.require())
                .map(existing -> {
                    track.setId(id);
                    track.setTenantId(TenantContext.require());
                    return ResponseEntity.ok(service.update(track));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Boolean> validate(@PathVariable UUID id) {
        return ResponseEntity.ok(service.validate(id, TenantContext.require()));
    }

    @PostMapping("/{id}/simulate")
    public ResponseEntity<Object> simulate(@PathVariable UUID id) {
        return ResponseEntity.ok(service.simulate(id, TenantContext.require()));
    }
}
