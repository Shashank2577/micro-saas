package com.microsaas.contractsense.service;

import com.microsaas.contractsense.domain.ContractClause;
import com.microsaas.contractsense.domain.RiskLevel;
import com.microsaas.contractsense.repository.ContractClauseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClauseExtractorService {

    private final ContractClauseRepository clauseRepository;

    @Transactional
    public List<ContractClause> extractClauses(UUID contractId, String content, UUID tenantId) {
        // Mock clause extraction with realistic types
        List<ContractClause> clauses = List.of(
            ContractClause.builder()
                .contractId(contractId)
                .clauseType("PAYMENT_TERMS")
                .content("Net 30 days upon receipt of invoice.")
                .riskLevel(RiskLevel.LOW)
                .deviationFromStandard(false)
                .tenantId(tenantId)
                .build(),
            ContractClause.builder()
                .contractId(contractId)
                .clauseType("LIABILITY_CAP")
                .content("Liability capped at 12 months of fees.")
                .riskLevel(RiskLevel.MEDIUM)
                .deviationFromStandard(true)
                .tenantId(tenantId)
                .build(),
            ContractClause.builder()
                .contractId(contractId)
                .clauseType("TERMINATION")
                .content("Either party may terminate with 60 days notice.")
                .riskLevel(RiskLevel.LOW)
                .deviationFromStandard(false)
                .tenantId(tenantId)
                .build(),
            ContractClause.builder()
                .contractId(contractId)
                .clauseType("INDEMNIFICATION")
                .content("Provider indemnifies Customer against all IP claims.")
                .riskLevel(RiskLevel.HIGH)
                .deviationFromStandard(true)
                .tenantId(tenantId)
                .build(),
            ContractClause.builder()
                .contractId(contractId)
                .clauseType("GOVERNING_LAW")
                .content("Governing law is State of Delaware.")
                .riskLevel(RiskLevel.LOW)
                .deviationFromStandard(false)
                .tenantId(tenantId)
                .build()
        );

        return clauseRepository.saveAll(clauses);
    }
}
