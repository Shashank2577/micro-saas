package com.microsaas.retailintelligence.controller;

import com.microsaas.retailintelligence.dto.DemandForecastDto;
import com.microsaas.retailintelligence.service.RetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/forecasts")
@RequiredArgsConstructor
public class DemandForecastController {

    private final RetailService retailService;

    @GetMapping
    public ResponseEntity<List<DemandForecastDto>> getForecasts(@RequestParam UUID skuId) {
        return ResponseEntity.ok(retailService.getForecasts(skuId));
    }

    @PostMapping("/generate")
    public ResponseEntity<Void> generateForecast(@RequestParam UUID skuId) {
        retailService.generateForecast(skuId);
        return ResponseEntity.ok().build();
    }
}
