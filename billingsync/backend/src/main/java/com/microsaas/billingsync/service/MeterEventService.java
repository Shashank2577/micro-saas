package com.microsaas.billingsync.service;

import com.microsaas.billingsync.exception.ResourceNotFoundException;
import com.microsaas.billingsync.exception.BadRequestException;
import com.microsaas.billingsync.model.MeterEvent;
import com.microsaas.billingsync.model.Subscription;
import com.microsaas.billingsync.model.PricingModel;
import com.microsaas.billingsync.repository.MeterEventRepository;
import com.microsaas.billingsync.repository.SubscriptionRepository;
import com.microsaas.billingsync.repository.PricingModelRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeterEventService {

    @Autowired
    private MeterEventRepository meterEventRepository;
    
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    
    @Autowired
    private PricingModelRepository pricingModelRepository;

    @Transactional
    public MeterEvent recordEvent(MeterEvent eventRequest) {
        String tenantId = TenantContext.require().toString();
        
        Subscription sub = subscriptionRepository.findByIdAndTenantId(eventRequest.getSubscription().getId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));
                
        if (!"ACTIVE".equals(sub.getStatus())) {
            throw new BadRequestException("Cannot record events for non-active subscriptions");
        }
        
        // Quota enforcement
        List<PricingModel> pricingModels = pricingModelRepository.findByPlanIdAndTenantId(sub.getPlan().getId(), tenantId);
        PricingModel matchingModel = pricingModels.stream()
            .filter(pm -> pm.getMetricName().equals(eventRequest.getMetricName()))
            .findFirst()
            .orElse(null);
            
        if (matchingModel != null) {
            Integer currentUsage = meterEventRepository.sumQuantityBySubscriptionAndMetricAndPeriod(
                tenantId, 
                sub.getId(), 
                eventRequest.getMetricName(), 
                sub.getCurrentPeriodStart(), 
                sub.getCurrentPeriodEnd()
            );
            
            if (currentUsage == null) currentUsage = 0;
            
            // Soft quota limit checking (just a warning/log in this implementation)
            if (currentUsage + eventRequest.getQuantity() > matchingModel.getIncludedUnits()) {
                System.out.println("Warning: Tenant " + tenantId + " exceeded included units for metric " + eventRequest.getMetricName());
            }
        }
                
        MeterEvent event = MeterEvent.builder()
                .tenantId(tenantId)
                .subscription(sub)
                .metricName(eventRequest.getMetricName())
                .quantity(eventRequest.getQuantity())
                .eventTimestamp(eventRequest.getEventTimestamp() != null ? eventRequest.getEventTimestamp() : LocalDateTime.now())
                .build();
                
        return meterEventRepository.save(event);
    }
}
