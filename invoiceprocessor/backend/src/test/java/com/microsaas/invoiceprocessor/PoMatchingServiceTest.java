package com.microsaas.invoiceprocessor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PoMatchingServiceTest {

    private PurchaseOrderRepository purchaseOrderRepository;
    private InvoiceRepository invoiceRepository;
    private InvoiceMatchRepository invoiceMatchRepository;
    private PoMatchingService poMatchingService;

    private UUID tenantId = UUID.randomUUID();
    private UUID vendorId = UUID.randomUUID();
    private UUID invoiceId = UUID.randomUUID();
    private UUID poId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        purchaseOrderRepository = Mockito.mock(PurchaseOrderRepository.class);
        invoiceRepository = Mockito.mock(InvoiceRepository.class);
        invoiceMatchRepository = Mockito.mock(InvoiceMatchRepository.class);
        poMatchingService = new PoMatchingService(purchaseOrderRepository, invoiceRepository, invoiceMatchRepository);
    }

    @Test
    void testMatchInvoice_Matched() {
        Invoice invoice = Invoice.builder()
                .id(invoiceId)
                .vendorId(vendorId)
                .amount(new BigDecimal("100.00"))
                .status(InvoiceStatus.RECEIVED)
                .tenantId(tenantId)
                .build();

        PurchaseOrder po = PurchaseOrder.builder()
                .id(poId)
                .vendorId(vendorId)
                .amount(new BigDecimal("102.00")) // Within 5%
                .status(PoStatus.OPEN)
                .tenantId(tenantId)
                .build();

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(purchaseOrderRepository.findByTenantIdAndVendorIdAndStatus(tenantId, vendorId, PoStatus.OPEN))
                .thenReturn(List.of(po));
        when(invoiceMatchRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        InvoiceMatch match = poMatchingService.matchInvoice(invoiceId, tenantId);

        assertEquals(MatchStatus.MATCHED, match.getMatchStatus());
        assertNull(match.getDiscrepancyDescription());
        assertEquals(PoStatus.MATCHED, po.getStatus());
        assertEquals(InvoiceStatus.MATCHED, invoice.getStatus());
    }

    @Test
    void testMatchInvoice_Mismatch() {
        Invoice invoice = Invoice.builder()
                .id(invoiceId)
                .vendorId(vendorId)
                .amount(new BigDecimal("110.00"))
                .status(InvoiceStatus.RECEIVED)
                .tenantId(tenantId)
                .build();

        PurchaseOrder po = PurchaseOrder.builder()
                .id(poId)
                .vendorId(vendorId)
                .amount(new BigDecimal("100.00")) // More than 5% difference
                .status(PoStatus.OPEN)
                .tenantId(tenantId)
                .build();

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(purchaseOrderRepository.findByTenantIdAndVendorIdAndStatus(tenantId, vendorId, PoStatus.OPEN))
                .thenReturn(List.of(po));
        when(invoiceMatchRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        InvoiceMatch match = poMatchingService.matchInvoice(invoiceId, tenantId);

        assertEquals(MatchStatus.MISMATCH, match.getMatchStatus());
        assertNotNull(match.getDiscrepancyDescription());
        assertEquals(PoStatus.OPEN, po.getStatus()); // Still open since mismatched
        assertEquals(InvoiceStatus.RECEIVED, invoice.getStatus()); // Status unchanged
    }

    @Test
    void testMatchInvoice_NoPoFound() {
        Invoice invoice = Invoice.builder()
                .id(invoiceId)
                .vendorId(vendorId)
                .amount(new BigDecimal("100.00"))
                .status(InvoiceStatus.RECEIVED)
                .tenantId(tenantId)
                .build();

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(purchaseOrderRepository.findByTenantIdAndVendorIdAndStatus(tenantId, vendorId, PoStatus.OPEN))
                .thenReturn(List.of());

        assertThrows(IllegalStateException.class, () -> {
            poMatchingService.matchInvoice(invoiceId, tenantId);
        });
    }
}
