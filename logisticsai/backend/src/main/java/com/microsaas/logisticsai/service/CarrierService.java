package com.microsaas.logisticsai.service;

import com.microsaas.logisticsai.domain.CarrierPerformance;
import com.microsaas.logisticsai.repository.CarrierPerformanceRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CarrierService {

    private final CarrierPerformanceRepository repository;
    private final EventPublisherService eventPublisher;

    public CarrierService(CarrierPerformanceRepository repository, EventPublisherService eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(readOnly = true)
    public List<CarrierPerformance> getAllCarriers() {
        return repository.findByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public CarrierPerformance getCarrier(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Carrier not found"));
    }

    @Transactional
    public CarrierPerformance addCarrier(CarrierPerformance carrier) {
        carrier.setTenantId(TenantContext.require());
        // Simple mock of prediction for newly added carrier
        if (carrier.getOnTimeRate() == null) {
            carrier.setOnTimeRate(95.0);
        }
        if (carrier.getPredictedDelayMins() == null) {
            carrier.setPredictedDelayMins((int)((100 - carrier.getOnTimeRate()) * 2));
        }
        
        CarrierPerformance saved = repository.save(carrier);
        
        eventPublisher.publishEvent("carrier.performance.updated", Map.of(
            "carrierId", saved.getId(),
            "onTimeRate", saved.getOnTimeRate(),
            "predictedDelayMins", saved.getPredictedDelayMins()
        ));
        
        return saved;
    }
}
