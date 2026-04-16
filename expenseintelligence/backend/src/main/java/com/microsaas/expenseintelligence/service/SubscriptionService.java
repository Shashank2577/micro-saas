package com.microsaas.expenseintelligence.service;

import com.microsaas.expenseintelligence.model.BillingCycle;
import com.microsaas.expenseintelligence.model.Expense;
import com.microsaas.expenseintelligence.model.Subscription;
import com.microsaas.expenseintelligence.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public Subscription detectSubscription(Expense expense) {
        if (expense.getCategory() == com.microsaas.expenseintelligence.model.ExpenseCategory.SOFTWARE) {
            List<Subscription> existing = subscriptionRepository.findByTenantIdAndVendor(expense.getTenantId(), expense.getVendor());
            if (existing.isEmpty()) {
                Subscription sub = Subscription.builder()
                        .vendor(expense.getVendor())
                        .amount(expense.getAmount())
                        .billingCycle(BillingCycle.MONTHLY)
                        .category("SOFTWARE")
                        .isRedundant(false)
                        .tenantId(expense.getTenantId())
                        .build();
                return subscriptionRepository.save(sub);
            }
        }
        return null;
    }

    @Transactional
    public Subscription markRedundant(UUID subscriptionId) {
        Subscription sub = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));
        sub.setRedundant(true);
        return subscriptionRepository.save(sub);
    }

    public BigDecimal getSubscriptionSpend(UUID tenantId) {
        List<Subscription> subscriptions = subscriptionRepository.findByTenantId(tenantId);
        return subscriptions.stream()
                .filter(s -> !s.isRedundant())
                .map(s -> {
                    if (s.getBillingCycle() == BillingCycle.ANNUAL) {
                        return s.getAmount().divide(new BigDecimal("12"), 2, java.math.RoundingMode.HALF_UP);
                    }
                    return s.getAmount();
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
