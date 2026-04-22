package com.microsaas.investtracker.controller;

import com.microsaas.investtracker.dto.TaxLotDto;
import com.microsaas.investtracker.service.TaxLotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TaxLotController {
    private final TaxLotService taxLotService;

    @GetMapping("/holdings/{id}/tax-lots")
    public ResponseEntity<List<TaxLotDto>> getTaxLots(@PathVariable UUID id) {
        return ResponseEntity.ok(taxLotService.getTaxLots(id));
    }
}