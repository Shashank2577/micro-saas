package com.microsaas.restaurantintel.controller;

import com.microsaas.restaurantintel.domain.Ingredient;
import com.microsaas.restaurantintel.domain.PredictiveOrder;
import com.microsaas.restaurantintel.service.PredictiveOrderingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class PredictiveOrderingController {

    private final PredictiveOrderingService service;

    public PredictiveOrderingController(PredictiveOrderingService service) {
        this.service = service;
    }

    @GetMapping("/ingredients")
    public List<Ingredient> getIngredients() {
        return service.getIngredients();
    }

    @GetMapping("/orders/predictive")
    public List<PredictiveOrder> getOrders() {
        return service.getOrders();
    }

    @PostMapping("/orders/predictive/generate")
    public ResponseEntity<Void> generatePredictions() {
        service.generatePredictions();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/orders/predictive/{id}/status")
    public PredictiveOrder updateOrderStatus(@PathVariable UUID id, @RequestBody java.util.Map<String, String> body) {
        return service.updateOrderStatus(id, body.get("status"));
    }
}
