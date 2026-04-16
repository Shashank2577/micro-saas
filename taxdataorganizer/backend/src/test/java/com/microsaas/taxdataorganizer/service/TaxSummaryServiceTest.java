package com.microsaas.taxdataorganizer.service;

import com.microsaas.taxdataorganizer.model.TaxSummary;
import com.microsaas.taxdataorganizer.model.Transaction;
import com.microsaas.taxdataorganizer.model.TransactionCategory;
import com.microsaas.taxdataorganizer.model.TaxDocument;
import com.microsaas.taxdataorganizer.repository.TaxSummaryRepository;
import com.microsaas.taxdataorganizer.repository.TransactionRepository;
import com.microsaas.taxdataorganizer.repository.TaxDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaxSummaryServiceTest {

    @Mock
    private TaxSummaryRepository taxSummaryRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TaxDocumentRepository taxDocumentRepository;

    @InjectMocks
    private TaxSummaryService taxSummaryService;

    private UUID taxYearId;
    private UUID tenantId;

    @BeforeEach
    void setUp() {
        taxYearId = UUID.randomUUID();
        tenantId = UUID.randomUUID();
    }

    @Test
    void generateSummary_CalculatesTotalsCorrectly() {
        // Arrange
        Transaction revenue = Transaction.builder()
                .amount(new BigDecimal("10000.00"))
                .category(TransactionCategory.REVENUE)
                .isDeductible(false)
                .build();
                
        Transaction deduction = Transaction.builder()
                .amount(new BigDecimal("2000.00"))
                .category(TransactionCategory.SOFTWARE)
                .isDeductible(true)
                .build();

        when(transactionRepository.findAllByTaxYearIdAndTenantId(taxYearId, tenantId))
                .thenReturn(List.of(revenue, deduction));
                
        when(taxDocumentRepository.findAllByTaxYearIdAndTenantId(taxYearId, tenantId))
                .thenReturn(List.of(new TaxDocument(), new TaxDocument()));
                
        when(taxSummaryRepository.findByTaxYearIdAndTenantId(taxYearId, tenantId))
                .thenReturn(Optional.empty());
                
        when(taxSummaryRepository.save(any(TaxSummary.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        // Act
        TaxSummary summary = taxSummaryService.generateSummary(taxYearId, tenantId);

        // Assert
        assertNotNull(summary);
        assertEquals(new BigDecimal("10000.00"), summary.getTotalRevenue());
        assertEquals(new BigDecimal("2000.00"), summary.getTotalDeductions());
        assertEquals(new BigDecimal("8000.00"), summary.getNetTaxableIncome());
        assertEquals(2, summary.getTransactionCount());
        assertEquals(2, summary.getDocumentCount());
    }

    @Test
    void generateSummary_UpdatesExistingSummary() {
        // Arrange
        when(transactionRepository.findAllByTaxYearIdAndTenantId(taxYearId, tenantId))
                .thenReturn(List.of());
                
        when(taxDocumentRepository.findAllByTaxYearIdAndTenantId(taxYearId, tenantId))
                .thenReturn(List.of());
                
        TaxSummary existingSummary = TaxSummary.builder()
                .id(UUID.randomUUID())
                .taxYearId(taxYearId)
                .tenantId(tenantId)
                .build();
                
        when(taxSummaryRepository.findByTaxYearIdAndTenantId(taxYearId, tenantId))
                .thenReturn(Optional.of(existingSummary));
                
        when(taxSummaryRepository.save(any(TaxSummary.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        // Act
        TaxSummary summary = taxSummaryService.generateSummary(taxYearId, tenantId);

        // Assert
        assertNotNull(summary);
        assertEquals(existingSummary.getId(), summary.getId());
        assertEquals(BigDecimal.ZERO, summary.getTotalRevenue());
        assertEquals(BigDecimal.ZERO, summary.getTotalDeductions());
        assertEquals(BigDecimal.ZERO, summary.getNetTaxableIncome());
    }

    @Test
    void generateSummary_HandlesOnlyDeductions() {
        // Arrange
        Transaction deduction1 = Transaction.builder()
                .amount(new BigDecimal("500.00"))
                .category(TransactionCategory.MEALS)
                .isDeductible(true)
                .build();
                
        Transaction deduction2 = Transaction.builder()
                .amount(new BigDecimal("1500.00"))
                .category(TransactionCategory.TRAVEL)
                .isDeductible(true)
                .build();

        when(transactionRepository.findAllByTaxYearIdAndTenantId(taxYearId, tenantId))
                .thenReturn(List.of(deduction1, deduction2));
                
        when(taxDocumentRepository.findAllByTaxYearIdAndTenantId(taxYearId, tenantId))
                .thenReturn(List.of());
                
        when(taxSummaryRepository.findByTaxYearIdAndTenantId(taxYearId, tenantId))
                .thenReturn(Optional.empty());
                
        when(taxSummaryRepository.save(any(TaxSummary.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        // Act
        TaxSummary summary = taxSummaryService.generateSummary(taxYearId, tenantId);

        // Assert
        assertNotNull(summary);
        assertEquals(BigDecimal.ZERO, summary.getTotalRevenue());
        assertEquals(new BigDecimal("2000.00"), summary.getTotalDeductions());
        assertEquals(new BigDecimal("-2000.00"), summary.getNetTaxableIncome());
        assertEquals(2, summary.getTransactionCount());
        assertEquals(0, summary.getDocumentCount());
    }
}
