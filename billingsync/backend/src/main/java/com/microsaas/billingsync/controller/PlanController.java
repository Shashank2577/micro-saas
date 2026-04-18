package com.microsaas.billingsync.controller;

import com.microsaas.billingsync.model.PricingModel;
import com.microsaas.billingsync.model.SubscriptionPlan;
import com.microsaas.billingsync.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

    @Autowired
    private PlanService planService;

    @GetMapping
    public List<SubscriptionPlan> getAllPlans() {
        return planService.getAllPlans();
    }

    @PostMapping
    public SubscriptionPlan createPlan(@RequestBody SubscriptionPlan plan) {
        return planService.createPlan(plan);
    }

    @GetMapping("/{id}")
    public SubscriptionPlan getPlanById(@PathVariable UUID id) {
        return planService.getPlanById(id);
    }

    @PostMapping("/{id}/pricing-models")
    public PricingModel addPricingModel(@PathVariable UUID id, @RequestBody PricingModel pricingModel) {
        return planService.addPricingModelToPlan(id, pricingModel);
    }
}
