package com.microsaas.dealbrain.service;

import com.microsaas.dealbrain.model.Deal;
import com.microsaas.dealbrain.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CrmSyncService {

    private final DealRepository dealRepository;

    /**
     * Simulates syncing deal state from an external CRM.
     */
    public void syncDealFromCrm(UUID tenantId, UUID dealId, String stage, Double amount) {
        Deal deal = dealRepository.findById(dealId).orElseThrow();
        if (!deal.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Tenant mismatch");
        }

        deal.setStage(stage);
        if (amount != null) {
            deal.setAmount(amount);
        }
        dealRepository.save(deal);
    }
}
