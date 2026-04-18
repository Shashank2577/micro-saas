package com.microsaas.telemetrycore.service;

import com.microsaas.telemetrycore.model.Funnel;
import com.microsaas.telemetrycore.model.Event;
import com.microsaas.telemetrycore.repository.FunnelRepository;
import com.microsaas.telemetrycore.repository.EventRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

@Service
public class FunnelService {
    @Autowired
    private FunnelRepository funnelRepository;
    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public Funnel createFunnel(Funnel funnel) {
        funnel.setTenantId(TenantContext.require());
        funnel.setCreatedAt(ZonedDateTime.now());
        funnel.setUpdatedAt(ZonedDateTime.now());
        return funnelRepository.save(funnel);
    }

    @Transactional(readOnly = true)
    public List<Funnel> getAllFunnels() {
        return funnelRepository.findByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public Funnel getFunnel(UUID id) {
        return funnelRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Funnel not found"));
    }

    @Transactional(readOnly = true)
    public Map<String, Object> analyzeFunnel(UUID id) {
        Funnel funnel = getFunnel(id);
        Map<String, Object> analysis = new HashMap<>();
        
        List<Event> allEvents = eventRepository.findByTenantIdAndTimestampBetween(
            TenantContext.require(), 
            ZonedDateTime.now().minusDays(30), 
            ZonedDateTime.now()
        );

        if (allEvents.isEmpty()) {
            funnel.getSteps().forEach(step -> analysis.put(step, 0));
            return analysis;
        }

        List<String> steps = funnel.getSteps();
        long previousCount = 0;

        for (int i = 0; i < steps.size(); i++) {
            String step = steps.get(i);
            long stepCount = allEvents.stream()
                .filter(e -> step.equals(e.getEventName()))
                .map(Event::getUserId)
                .distinct()
                .count();

            if (i == 0) {
                analysis.put(step, stepCount > 0 ? 100 : 0);
                previousCount = stepCount;
            } else {
                if (previousCount == 0) {
                    analysis.put(step, 0);
                } else {
                    double percentage = ((double) Math.min(stepCount, previousCount) / previousCount) * 100;
                    analysis.put(step, Math.round(percentage));
                    previousCount = Math.min(stepCount, previousCount); 
                }
            }
        }

        return analysis;
    }
}
