package com.microsaas.compensationos.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.compensationos.entity.DepartmentBudget;
import com.microsaas.compensationos.repository.DepartmentBudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentBudgetService {
    private final DepartmentBudgetRepository repository;

    public List<DepartmentBudget> getAll() {
        return repository.findByTenantId(TenantContext.require());
    }

    public DepartmentBudget save(DepartmentBudget entity) {
        entity.setTenantId(TenantContext.require());
        return repository.save(entity);
    }
}
