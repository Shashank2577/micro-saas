package com.microsaas.compensationos.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.compensationos.entity.BenefitPlan;
import com.microsaas.compensationos.repository.BenefitPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BenefitPlanService {
    private final BenefitPlanRepository repository;

    public List<BenefitPlan> getAll() {
        return repository.findByTenantId(TenantContext.require());
    }

    public BenefitPlan save(BenefitPlan entity) {
        entity.setTenantId(TenantContext.require());
        return repository.save(entity);
    }
}
