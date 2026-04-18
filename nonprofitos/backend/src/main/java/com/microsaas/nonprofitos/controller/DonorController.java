package com.microsaas.nonprofitos.controller;

import com.microsaas.nonprofitos.domain.Donor;
import com.microsaas.nonprofitos.dto.DonorDto;
import com.microsaas.nonprofitos.service.DonorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/donors")
public class DonorController {

    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @GetMapping
    public ResponseEntity<List<Donor>> getAllDonors() {
        return ResponseEntity.ok(donorService.getAllDonors());
    }

    @PostMapping
    public ResponseEntity<Donor> createDonor(@RequestBody DonorDto dto) {
        return ResponseEntity.ok(donorService.createDonor(dto));
    }

    @GetMapping("/{id}/intelligence")
    public ResponseEntity<String> getDonorIntelligence(@PathVariable UUID id) {
        return ResponseEntity.ok(donorService.getDonorIntelligence(id));
    }
}
