package com.microsaas.contractportfolio.service;

import com.microsaas.contractportfolio.domain.ContractRecord;
import com.microsaas.contractportfolio.domain.ContractStatus;
import com.microsaas.contractportfolio.repository.ContractRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContractRecordService {

    private final ContractRecordRepository contractRecordRepository;

    @Transactional
    public ContractRecord ingestContract(String counterparty, String contractType, LocalDate startDate, LocalDate endDate, boolean autoRenews, int renewalNoticeDays, UUID tenantId) {
        ContractRecord contract = ContractRecord.builder()
                .counterparty(counterparty)
                .contractType(contractType)
                .startDate(startDate)
                .endDate(endDate)
                .autoRenews(autoRenews)
                .renewalNoticeDays(renewalNoticeDays)
                .tenantId(tenantId)
                .status(ContractStatus.ACTIVE)
                .build();
        return contractRecordRepository.save(contract);
    }

    public List<ContractRecord> listContracts(UUID tenantId) {
        return contractRecordRepository.findByTenantId(tenantId);
    }

    public List<ContractRecord> getExpiringContracts(UUID tenantId, int daysAhead) {
        LocalDate today = LocalDate.now();
        LocalDate targetDate = today.plusDays(daysAhead);
        return contractRecordRepository.findByTenantIdAndEndDateBetween(tenantId, today, targetDate);
    }

    @Transactional
    public ContractRecord terminateContract(UUID id, UUID tenantId) {
        ContractRecord contract = contractRecordRepository.findById(id)
                .filter(c -> c.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));
        contract.setStatus(ContractStatus.TERMINATED);
        return contractRecordRepository.save(contract);
    }
}
