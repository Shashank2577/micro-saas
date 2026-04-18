package com.microsaas.telemetrycore.service;

import com.microsaas.telemetrycore.model.Cohort;
import com.microsaas.telemetrycore.model.Event;
import com.microsaas.telemetrycore.repository.CohortRepository;
import com.microsaas.telemetrycore.repository.EventRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CohortService {
    @Autowired
    private CohortRepository cohortRepository;
    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public Cohort createCohort(Cohort cohort) {
        cohort.setTenantId(TenantContext.require());
        cohort.setCreatedAt(ZonedDateTime.now());
        cohort.setUpdatedAt(ZonedDateTime.now());
        return cohortRepository.save(cohort);
    }

    @Transactional(readOnly = true)
    public List<Cohort> getAllCohorts() {
        return cohortRepository.findByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public Cohort getCohort(UUID id) {
        return cohortRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Cohort not found"));
    }
    
    @Transactional(readOnly = true)
    public List<String> getCohortUsers(UUID id) {
        Cohort cohort = getCohort(id);
        
        List<Event> allEvents = eventRepository.findByTenantIdAndTimestampBetween(
            TenantContext.require(), 
            ZonedDateTime.now().minusDays(90), 
            ZonedDateTime.now()
        );
        
        String targetEvent = (String) cohort.getCriteria().get("eventName");
        if (targetEvent == null) return List.of();

        return allEvents.stream()
            .filter(e -> targetEvent.equals(e.getEventName()))
            .map(Event::getUserId)
            .filter(userId -> userId != null)
            .distinct()
            .collect(Collectors.toList());
    }
}
