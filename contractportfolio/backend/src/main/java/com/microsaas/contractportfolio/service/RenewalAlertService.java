package com.microsaas.contractportfolio.service;

import com.microsaas.contractportfolio.domain.ContractRecord;
import com.microsaas.contractportfolio.domain.RenewalAlert;
import com.microsaas.contractportfolio.repository.ContractRecordRepository;
import com.microsaas.contractportfolio.repository.RenewalAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RenewalAlertService {

    private final RenewalAlertRepository renewalAlertRepository;
    private final ContractRecordRepository contractRecordRepository;

    @Transactional
    public List<RenewalAlert> generateRenewalAlerts(UUID contractId, UUID tenantId) {
        ContractRecord contract = contractRecordRepository.findById(contractId)
                .filter(c -> c.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));

        List<RenewalAlert> alerts = new ArrayList<>();
        LocalDate renewalDate = contract.getEndDate();
        int renewalNoticeDays = contract.getRenewalNoticeDays();
        
        if (renewalNoticeDays > 0) {
            alerts.add(RenewalAlert.builder()
                    .contractId(contractId)
                    .tenantId(tenantId)
                    .alertDate(renewalDate.minusDays(renewalNoticeDays))
                    .daysUntilRenewal(renewalNoticeDays)
                    .build());
                    
            if (renewalNoticeDays / 2 > 0) {
                 alerts.add(RenewalAlert.builder()
                        .contractId(contractId)
                        .tenantId(tenantId)
                        .alertDate(renewalDate.minusDays(renewalNoticeDays / 2))
                        .daysUntilRenewal(renewalNoticeDays / 2)
                        .build());
            }
        }

        alerts.add(RenewalAlert.builder()
                .contractId(contractId)
                .tenantId(tenantId)
                .alertDate(renewalDate.minusDays(7))
                .daysUntilRenewal(7)
                .build());

        return renewalAlertRepository.saveAll(alerts);
    }

    public List<RenewalAlert> listAlerts(UUID tenantId) {
        return renewalAlertRepository.findByTenantId(tenantId);
    }

    @Transactional
    public RenewalAlert acknowledgeAlert(UUID alertId, String acknowledgedBy, UUID tenantId) {
        RenewalAlert alert = renewalAlertRepository.findById(alertId)
                .filter(a -> a.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Alert not found"));
        alert.setAcknowledgedBy(acknowledgedBy);
        return renewalAlertRepository.save(alert);
    }
}
