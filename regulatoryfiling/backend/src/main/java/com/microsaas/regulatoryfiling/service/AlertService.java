package com.microsaas.regulatoryfiling.service;

import com.microsaas.regulatoryfiling.domain.FilingAlert;
import com.microsaas.regulatoryfiling.domain.FilingObligation;
import com.microsaas.regulatoryfiling.repository.FilingAlertRepository;
import com.microsaas.regulatoryfiling.repository.FilingObligationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final FilingAlertRepository alertRepository;
    private final FilingObligationRepository obligationRepository;

    @Transactional
    public void generateAlerts(UUID obligationId, UUID tenantId) {
        FilingObligation obligation = obligationRepository.findByIdAndTenantId(obligationId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Obligation not found"));

        int[] alertDays = {30, 14, 7};
        for (int days : alertDays) {
            LocalDate alertDate = obligation.getDueDate().minusDays(days);
            FilingAlert alert = FilingAlert.builder()
                    .id(UUID.randomUUID())
                    .obligationId(obligationId)
                    .alertDate(alertDate)
                    .daysUntilDue(days)
                    .acknowledged(false)
                    .tenantId(tenantId)
                    .build();
            alertRepository.save(alert);
        }
    }

    @Transactional(readOnly = true)
    public List<FilingAlert> listAlerts(UUID tenantId) {
        return alertRepository.findByTenantId(tenantId);
    }

    @Transactional
    public FilingAlert acknowledgeAlert(UUID id, UUID tenantId) {
        FilingAlert alert = alertRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found"));
        alert.setAcknowledged(true);
        return alertRepository.save(alert);
    }
}
