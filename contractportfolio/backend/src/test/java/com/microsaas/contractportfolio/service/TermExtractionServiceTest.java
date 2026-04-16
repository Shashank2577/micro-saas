package com.microsaas.contractportfolio.service;

import com.microsaas.contractportfolio.domain.ContractRecord;
import com.microsaas.contractportfolio.domain.ContractStatus;
import com.microsaas.contractportfolio.domain.ExtractedTerm;
import com.microsaas.contractportfolio.domain.TermType;
import com.microsaas.contractportfolio.repository.ContractRecordRepository;
import com.microsaas.contractportfolio.repository.ExtractedTermRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TermExtractionServiceTest {

    @Mock
    private ExtractedTermRepository extractedTermRepository;

    @InjectMocks
    private TermExtractionService termExtractionService;

    private UUID contractId;
    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        contractId = UUID.randomUUID();
        
        when(extractedTermRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void extractTerms_CreatesFourStandardTerms() {
        List<ExtractedTerm> terms = termExtractionService.extractTerms(contractId, "mock raw content", tenantId);
        
        assertThat(terms).hasSize(4);
        assertThat(terms).extracting(ExtractedTerm::getTermType)
                .containsExactlyInAnyOrder(
                        TermType.PAYMENT_TERMS,
                        TermType.LIABILITY_CAP,
                        TermType.TERMINATION_NOTICE,
                        TermType.SLA_COMMITMENT
                );
    }

    @Test
    void extractTerms_AllTermsHaveConfidenceGreaterThan80() {
        List<ExtractedTerm> terms = termExtractionService.extractTerms(contractId, "mock raw content", tenantId);
        
        for (ExtractedTerm term : terms) {
            assertThat(term.getConfidenceScore()).isGreaterThan(0.80);
        }
    }

    @Test
    void extractTerms_PaymentTermsContainsNet30() {
        List<ExtractedTerm> terms = termExtractionService.extractTerms(contractId, "mock raw content", tenantId);
        
        ExtractedTerm paymentTerm = terms.stream()
                .filter(t -> t.getTermType() == TermType.PAYMENT_TERMS)
                .findFirst()
                .orElseThrow();
                
        assertThat(paymentTerm.getValue()).contains("net-30");
    }
}
