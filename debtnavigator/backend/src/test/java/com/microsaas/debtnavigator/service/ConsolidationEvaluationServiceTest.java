package com.microsaas.debtnavigator.service;

import com.microsaas.debtnavigator.dto.ConsolidationEvaluationDto;
import com.microsaas.debtnavigator.entity.ConsolidationLoan;
import com.microsaas.debtnavigator.entity.Debt;
import com.microsaas.debtnavigator.repository.ConsolidationLoanRepository;
import com.microsaas.debtnavigator.repository.DebtRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class ConsolidationEvaluationServiceTest {

    @Mock
    private DebtRepository debtRepository;

    @Mock
    private ConsolidationLoanRepository consolidationLoanRepository;

    @InjectMocks
    private ConsolidationEvaluationService consolidationEvaluationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEvaluateConsolidation() {
        String tenantId = "test-tenant";

        Debt debt1 = Debt.builder()
                .tenantId(tenantId)
                .name("Credit Card")
                .balance(new BigDecimal("5000.00"))
                .apr(new BigDecimal("22.00"))
                .build();

        ConsolidationLoan loan = ConsolidationLoan.builder()
                .tenantId(tenantId)
                .providerName("LendingClub")
                .apr(new BigDecimal("10.00"))
                .originationFee(new BigDecimal("50.00"))
                .maxAmount(new BigDecimal("10000.00"))
                .build();

        when(debtRepository.findByTenantId(tenantId)).thenReturn(Arrays.asList(debt1));
        when(consolidationLoanRepository.findByTenantId(tenantId)).thenReturn(Arrays.asList(loan));

        List<ConsolidationEvaluationDto> results = consolidationEvaluationService.evaluateConsolidation(tenantId);

        assertEquals(1, results.size());
        assertTrue(results.get(0).isRecommended());
        assertEquals(new BigDecimal("22.0000"), results.get(0).getBlendedCurrentApr());
    }
}
