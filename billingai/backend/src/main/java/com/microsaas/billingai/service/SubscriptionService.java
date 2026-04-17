package com.microsaas.billingai.service;

import com.microsaas.billingai.model.Subscription;
import com.microsaas.billingai.model.SubscriptionStatus;
import com.microsaas.billingai.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public Subscription createSubscription(Subscription subscription) {
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        return subscriptionRepository.save(subscription);
    }

    public List<Subscription> getSubscriptionsByTenant(UUID tenantId) {
        return subscriptionRepository.findByTenantId(tenantId);
    }

    public Subscription getSubscriptionById(UUID id, UUID tenantId) {
        return subscriptionRepository.findById(id)
                .filter(s -> s.getTenantId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
    }

    @Transactional
    public Subscription updateSubscriptionStatus(UUID id, SubscriptionStatus status, UUID tenantId) {
        Subscription subscription = getSubscriptionById(id, tenantId);
        subscription.setStatus(status);
        return subscriptionRepository.save(subscription);
    }
}
