package com.microsaas.compensationos.controller;

import com.microsaas.compensationos.entity.DepartmentBudget;
import com.microsaas.compensationos.service.DepartmentBudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/department-budgets")
@RequiredArgsConstructor
public class DepartmentBudgetController {
    private final DepartmentBudgetService service;

    @GetMapping
    public List<DepartmentBudget> getAll() {
        return service.getAll();
    }

    @PostMapping
    public DepartmentBudget create(@RequestBody DepartmentBudget entity) {
        return service.save(entity);
    }
}
