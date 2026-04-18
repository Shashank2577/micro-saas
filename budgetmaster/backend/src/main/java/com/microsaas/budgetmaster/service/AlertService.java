package com.microsaas.budgetmaster.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.budgetmaster.domain.Alert;
import com.microsaas.budgetmaster.domain.Category;
import com.microsaas.budgetmaster.domain.Transaction;
import com.microsaas.budgetmaster.dto.Requests.CreateAlertRequest;
import com.microsaas.budgetmaster.repository.AlertRepository;
import com.microsaas.budgetmaster.repository.CategoryRepository;
import com.microsaas.budgetmaster.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertService {
    private final AlertRepository alertRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<Alert> getAllAlerts() {
        return alertRepository.findAllByTenantId(TenantContext.require());
    }

    @Transactional
    public Alert createAlert(CreateAlertRequest request) {
        Alert alert = Alert.builder()
                .tenantId(TenantContext.require())
                .categoryId(request.getCategoryId())
                .thresholdPercent(request.getThresholdPercent())
                .thresholdAmount(request.getThresholdAmount())
                .triggered(false)
                .build();
        return alertRepository.save(alert);
    }

    @Transactional
    public void checkAlerts(UUID categoryId) {
        UUID tenantId = TenantContext.require();
        Category category = categoryRepository.findByIdAndTenantId(categoryId, tenantId).orElse(null);
        if (category == null) return;

        List<Alert> alerts = alertRepository.findAllByCategoryIdAndTenantId(categoryId, tenantId);
        if (alerts.isEmpty()) return;

        List<Transaction> transactions = transactionRepository.findAllByCategoryIdAndTenantId(categoryId, tenantId);
        BigDecimal totalSpent = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        for (Alert alert : alerts) {
            boolean trigger = false;
            if (alert.getThresholdAmount() != null && totalSpent.compareTo(alert.getThresholdAmount()) >= 0) {
                trigger = true;
            } else if (alert.getThresholdPercent() != null && category.getAllocatedAmount().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal percentSpent = totalSpent.divide(category.getAllocatedAmount(), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
                if (percentSpent.compareTo(alert.getThresholdPercent()) >= 0) {
                    trigger = true;
                }
            }

            if (trigger && !alert.isTriggered()) {
                alert.setTriggered(true);
                alertRepository.save(alert);
                String msg = "Alert triggered for category " + category.getName() + " at " + alert.getThresholdPercent() + "%";
                log.info(msg);
                
                // Spring event simulation of pgmq async publish
                eventPublisher.publishEvent(Map.of(
                        "type", "budget.alert.triggered",
                        "budgetId", category.getBudgetId().toString(),
                        "categoryId", category.getId().toString(),
                        "message", msg
                ));
            }
        }
    }
}
