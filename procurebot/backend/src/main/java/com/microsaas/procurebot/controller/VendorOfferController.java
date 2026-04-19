package com.microsaas.procurebot.controller;

import com.microsaas.procurebot.dto.VendorOfferRequest;
import com.microsaas.procurebot.model.VendorOffer;
import com.microsaas.procurebot.service.VendorOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/procurement/vendor-offers")
@RequiredArgsConstructor
public class VendorOfferController {

    private final VendorOfferService service;

    // TODO: Fetch tenantId from authentication context, currently hardcoded or passed as header for MVP
    private UUID getTenantId() {
        return UUID.fromString("00000000-0000-0000-0000-000000000001");
    }

    @GetMapping
    public ResponseEntity<List<VendorOffer>> getAll() {
        return ResponseEntity.ok(service.findAll(getTenantId()));
    }

    @PostMapping
    public ResponseEntity<VendorOffer> create(@RequestBody VendorOfferRequest request) {
        return ResponseEntity.ok(service.create(getTenantId(), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorOffer> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id, getTenantId()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VendorOffer> update(@PathVariable UUID id, @RequestBody VendorOfferRequest request) {
        return ResponseEntity.ok(service.update(id, getTenantId(), request));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id) {
        service.validate(id, getTenantId());
        return ResponseEntity.ok().build();
    }
}
