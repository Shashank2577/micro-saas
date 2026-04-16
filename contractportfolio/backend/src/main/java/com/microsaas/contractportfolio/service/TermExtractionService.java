package com.microsaas.contractportfolio.service;

import com.microsaas.contractportfolio.domain.ExtractedTerm;
import com.microsaas.contractportfolio.domain.TermType;
import com.microsaas.contractportfolio.repository.ExtractedTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TermExtractionService {

    private final ExtractedTermRepository extractedTermRepository;

    @Transactional
    public List<ExtractedTerm> extractTerms(UUID contractId, String rawContent, UUID tenantId) {
        // mock implementation based on requirements
        List<ExtractedTerm> terms = new ArrayList<>();

        terms.add(ExtractedTerm.builder()
                .contractId(contractId)
                .tenantId(tenantId)
                .termType(TermType.PAYMENT_TERMS)
                .value("net-30")
                .confidenceScore(0.95)
                .sourceText("Payment shall be made net-30 days from receipt of invoice.")
                .build());

        terms.add(ExtractedTerm.builder()
                .contractId(contractId)
                .tenantId(tenantId)
                .termType(TermType.LIABILITY_CAP)
                .value("1M")
                .confidenceScore(0.90)
                .sourceText("Liability is capped at 1M USD.")
                .build());

        terms.add(ExtractedTerm.builder()
                .contractId(contractId)
                .tenantId(tenantId)
                .termType(TermType.TERMINATION_NOTICE)
                .value("30 days notice")
                .confidenceScore(0.88)
                .sourceText("Either party may terminate with 30 days notice.")
                .build());

        terms.add(ExtractedTerm.builder()
                .contractId(contractId)
                .tenantId(tenantId)
                .termType(TermType.SLA_COMMITMENT)
                .value("99.9% uptime")
                .confidenceScore(0.86)
                .sourceText("Provider guarantees 99.9% uptime.")
                .build());

        return extractedTermRepository.saveAll(terms);
    }

    public List<ExtractedTerm> getTerms(UUID contractId, UUID tenantId) {
        return extractedTermRepository.findByContractIdAndTenantId(contractId, tenantId);
    }
}
