package com.microsaas.billingsync.service;

import com.microsaas.billingsync.exception.ResourceNotFoundException;
import com.microsaas.billingsync.model.PricingModel;
import com.microsaas.billingsync.model.SubscriptionPlan;
import com.microsaas.billingsync.repository.PricingModelRepository;
import com.microsaas.billingsync.repository.SubscriptionPlanRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PlanService {

    @Autowired
    private SubscriptionPlanRepository planRepository;

    @Autowired
    private PricingModelRepository pricingModelRepository;

    public List<SubscriptionPlan> getAllPlans() {
        String tenantId = TenantContext.require().toString();
        return planRepository.findByTenantId(tenantId);
    }

    public SubscriptionPlan getPlanById(UUID id) {
        String tenantId = TenantContext.require().toString();
        return planRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));
    }

    @Transactional
    public SubscriptionPlan createPlan(SubscriptionPlan plan) {
        String tenantId = TenantContext.require().toString();
        plan.setTenantId(tenantId);
        return planRepository.save(plan);
    }

    @Transactional
    public PricingModel addPricingModelToPlan(UUID planId, PricingModel pricingModel) {
        String tenantId = TenantContext.require().toString();
        SubscriptionPlan plan = getPlanById(planId);
        
        pricingModel.setTenantId(tenantId);
        pricingModel.setPlan(plan);
        return pricingModelRepository.save(pricingModel);
    }
}
