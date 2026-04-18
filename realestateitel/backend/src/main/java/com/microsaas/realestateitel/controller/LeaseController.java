package com.microsaas.realestateitel.controller;

import com.microsaas.realestateitel.domain.Lease;
import com.microsaas.realestateitel.service.LeaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LeaseController {

    private final LeaseService leaseService;

    @PostMapping("/leases/upload")
    public ResponseEntity<Lease> uploadLease(@RequestBody Map<String, String> payload) {
        UUID propertyId = UUID.fromString(payload.get("propertyId"));
        String text = payload.get("text");
        return ResponseEntity.ok(leaseService.processLeaseText(text, propertyId));
    }

    @GetMapping("/leases/{id}")
    public ResponseEntity<Lease> getLease(@PathVariable UUID id) {
        return ResponseEntity.ok(leaseService.getLeaseById(id));
    }

    @GetMapping("/properties/{propertyId}/leases")
    public ResponseEntity<List<Lease>> getLeasesForProperty(@PathVariable UUID propertyId) {
        return ResponseEntity.ok(leaseService.getLeasesForProperty(propertyId));
    }
}
