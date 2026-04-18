package com.microsaas.billingsync.service;

import com.microsaas.billingsync.exception.ResourceNotFoundException;
import com.microsaas.billingsync.exception.BadRequestException;
import com.microsaas.billingsync.model.Subscription;
import com.microsaas.billingsync.model.SubscriptionPlan;
import com.microsaas.billingsync.repository.SubscriptionRepository;
import com.microsaas.billingsync.repository.SubscriptionPlanRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SubscriptionPlanRepository planRepository;
    
    @Autowired
    private BillingService billingService;

    public List<Subscription> getAllSubscriptions() {
        String tenantId = TenantContext.require().toString();
        return subscriptionRepository.findByTenantId(tenantId);
    }

    public Subscription getSubscriptionById(UUID id) {
        String tenantId = TenantContext.require().toString();
        return subscriptionRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));
    }

    @Transactional
    public Subscription createSubscription(UUID planId, String paymentMethodId) {
        String tenantId = TenantContext.require().toString();
        SubscriptionPlan plan = planRepository.findByIdAndTenantId(planId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = plan.getBillingPeriod().equals("MONTHLY") ? now.plusMonths(1) : now.plusYears(1);

        Subscription sub = Subscription.builder()
                .tenantId(tenantId)
                .plan(plan)
                .status("ACTIVE")
                .paymentMethodId(paymentMethodId)
                .currentPeriodStart(now)
                .currentPeriodEnd(end)
                .build();
                
        sub = subscriptionRepository.save(sub);
        
        // Generate initial invoice for base price
        billingService.generateInvoice(sub.getId(), now, end, true);
        
        return sub;
    }

    @Transactional
    public Subscription changePlan(UUID subscriptionId, UUID newPlanId) {
        String tenantId = TenantContext.require().toString();
        Subscription subscription = getSubscriptionById(subscriptionId);
        SubscriptionPlan oldPlan = subscription.getPlan();
        
        SubscriptionPlan newPlan = planRepository.findByIdAndTenantId(newPlanId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("New plan not found"));

        if (oldPlan.getId().equals(newPlan.getId())) {
            throw new BadRequestException("Subscription is already on this plan");
        }

        LocalDateTime now = LocalDateTime.now();
        
        // Calculate proration
        long totalDaysInPeriod = ChronoUnit.DAYS.between(subscription.getCurrentPeriodStart(), subscription.getCurrentPeriodEnd());
        if (totalDaysInPeriod == 0) totalDaysInPeriod = 1; // Prevent division by zero
        
        long remainingDays = ChronoUnit.DAYS.between(now, subscription.getCurrentPeriodEnd());
        if (remainingDays < 0) remainingDays = 0;
        
        BigDecimal remainingFraction = new BigDecimal(remainingDays).divide(new BigDecimal(totalDaysInPeriod), 4, RoundingMode.HALF_UP);
        BigDecimal unusedOldPlanValue = oldPlan.getBasePrice().multiply(remainingFraction);
        
        // Update subscription to new plan
        subscription.setPlan(newPlan);
        subscription.setCurrentPeriodStart(now);
        LocalDateTime newEnd = newPlan.getBillingPeriod().equals("MONTHLY") ? now.plusMonths(1) : now.plusYears(1);
        subscription.setCurrentPeriodEnd(newEnd);
        
        subscription = subscriptionRepository.save(subscription);
        
        // Generate proration invoice
        billingService.generateProrationInvoice(subscriptionId, newPlan.getBasePrice(), unusedOldPlanValue);
        
        return subscription;
    }

    @Transactional
    public Subscription cancelSubscription(UUID id) {
        Subscription subscription = getSubscriptionById(id);
        subscription.setStatus("CANCELED");
        return subscriptionRepository.save(subscription);
    }
}
