package com.microsaas.compensationos.controller;

import com.microsaas.compensationos.entity.BenefitPlan;
import com.microsaas.compensationos.service.BenefitPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/benefit-plans")
@RequiredArgsConstructor
public class BenefitPlanController {
    private final BenefitPlanService service;

    @GetMapping
    public List<BenefitPlan> getAll() {
        return service.getAll();
    }

    @PostMapping
    public BenefitPlan create(@RequestBody BenefitPlan entity) {
        return service.save(entity);
    }
}
