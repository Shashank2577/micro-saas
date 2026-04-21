package com.microsaas.dealbrain.service;

import com.microsaas.dealbrain.model.DealActivity;
import com.microsaas.dealbrain.repository.DealActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailEngagementService {

    private final DealActivityRepository activityRepository;

    /**
     * Logs an email engagement activity.
     */
    public void logEmailEngagement(UUID tenantId, UUID dealId, String description) {
        DealActivity activity = new DealActivity();
        activity.setTenantId(tenantId);
        activity.setDealId(dealId);
        activity.setType("EMAIL");
        activity.setDescription(description);
        activity.setTimestamp(LocalDateTime.now());
        activityRepository.save(activity);
    }
}
