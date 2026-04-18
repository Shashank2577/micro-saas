package com.microsaas.insuranceai.controller;

import com.microsaas.insuranceai.domain.Claim;
import com.microsaas.insuranceai.service.ClaimService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @GetMapping
    public List<Claim> getClaims() {
        return claimService.getAllClaims();
    }

    @GetMapping("/{id}")
    public Claim getClaim(@PathVariable UUID id) {
        return claimService.getClaim(id);
    }

    @PostMapping
    public Claim createClaim(@RequestBody Claim claim) {
        return claimService.createClaim(claim);
    }

    @PutMapping("/{id}/status")
    public Claim updateStatus(@PathVariable UUID id, @RequestBody String status) {
        return claimService.updateStatus(id, status);
    }
}
