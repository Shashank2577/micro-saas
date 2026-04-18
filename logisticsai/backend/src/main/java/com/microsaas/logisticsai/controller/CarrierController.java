package com.microsaas.logisticsai.controller;

import com.microsaas.logisticsai.domain.CarrierPerformance;
import com.microsaas.logisticsai.service.CarrierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/carriers")
public class CarrierController {

    private final CarrierService service;

    public CarrierController(CarrierService service) {
        this.service = service;
    }

    @GetMapping
    public List<CarrierPerformance> getAllCarriers() {
        return service.getAllCarriers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarrierPerformance> getCarrier(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getCarrier(id));
    }

    @PostMapping
    public CarrierPerformance addCarrier(@RequestBody CarrierPerformance carrier) {
        return service.addCarrier(carrier);
    }
}
