package com.microsaas.contractsense.service;

import com.microsaas.contractsense.domain.Contract;
import com.microsaas.contractsense.domain.ContractStatus;
import com.microsaas.contractsense.domain.RiskAssessment;
import com.microsaas.contractsense.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final ClauseExtractorService clauseExtractorService;
    private final RiskAnalysisService riskAnalysisService;

    @Transactional
    public Contract uploadContract(String title, String counterparty, String contractType, String content, UUID tenantId) {
        Contract contract = Contract.builder()
                .title(title)
                .counterparty(counterparty)
                .contractType(contractType)
                .status(ContractStatus.REVIEW)
                .uploadedAt(LocalDateTime.now())
                .tenantId(tenantId)
                .build();

        Contract savedContract = contractRepository.save(contract);
        
        // Extract clauses
        clauseExtractorService.extractClauses(savedContract.getId(), content, tenantId);

        return savedContract;
    }

    public List<Contract> listContracts(UUID tenantId) {
        return contractRepository.findByTenantId(tenantId);
    }

    public Contract getContract(UUID id, UUID tenantId) {
        return contractRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found or not accessible"));
    }

    @Transactional
    public void deleteContract(UUID id, UUID tenantId) {
        Contract contract = getContract(id, tenantId);
        contractRepository.delete(contract);
    }

    @Transactional
    public RiskAssessment analyzeContract(UUID id, UUID tenantId) {
        Contract contract = getContract(id, tenantId);
        return riskAnalysisService.analyzeContract(contract.getId(), tenantId);
    }
}
