package com.microsaas.telemetrycore.service;

import com.microsaas.telemetrycore.model.Experiment;
import com.microsaas.telemetrycore.model.Event;
import com.microsaas.telemetrycore.repository.ExperimentRepository;
import com.microsaas.telemetrycore.repository.EventRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@Service
public class ExperimentService {
    @Autowired
    private ExperimentRepository experimentRepository;
    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public Experiment createExperiment(Experiment experiment) {
        experiment.setTenantId(TenantContext.require());
        experiment.setStatus("ACTIVE");
        experiment.setCreatedAt(ZonedDateTime.now());
        experiment.setUpdatedAt(ZonedDateTime.now());
        return experimentRepository.save(experiment);
    }

    @Transactional(readOnly = true)
    public List<Experiment> getAllExperiments() {
        return experimentRepository.findByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public Experiment getExperiment(UUID id) {
        return experimentRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Experiment not found"));
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getExperimentResults(UUID id) {
        Experiment experiment = getExperiment(id);
        Map<String, Object> results = new HashMap<>();
        
        List<Event> allEvents = eventRepository.findByTenantIdAndTimestampBetween(
            TenantContext.require(),
            experiment.getCreatedAt(),
            ZonedDateTime.now()
        );

        for (String variant : experiment.getVariants()) {
            long totalVariantAssignments = allEvents.stream()
                .filter(e -> "experiment_assigned".equals(e.getEventName()))
                .filter(e -> e.getProperties() != null && variant.equals(e.getProperties().get("variant")))
                .count();
                
            long variantConversions = allEvents.stream()
                .filter(e -> "experiment_converted".equals(e.getEventName()))
                .filter(e -> e.getProperties() != null && variant.equals(e.getProperties().get("variant")))
                .count();

            if (totalVariantAssignments == 0) {
                results.put(variant, 0.0);
            } else {
                double conversionRate = ((double) variantConversions / totalVariantAssignments) * 100;
                results.put(variant, conversionRate);
            }
        }
        return results;
    }
}
