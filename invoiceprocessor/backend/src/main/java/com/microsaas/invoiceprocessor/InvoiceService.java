package com.microsaas.invoiceprocessor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceLineItemRepository invoiceLineItemRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, InvoiceLineItemRepository invoiceLineItemRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceLineItemRepository = invoiceLineItemRepository;
    }

    @Transactional
    public Invoice receiveInvoice(Invoice invoice) {
        invoice.setStatus(InvoiceStatus.RECEIVED);
        invoice.setReceivedAt(LocalDateTime.now());
        if (invoice.getCurrency() == null) {
            invoice.setCurrency("USD");
        }
        return invoiceRepository.save(invoice);
    }

    @Transactional
    public List<InvoiceLineItem> extractLineItems(UUID invoiceId, UUID tenantId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        if (!invoice.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Invoice does not belong to tenant");
        }

        // Create 3 mock line items
        BigDecimal oneThird = invoice.getAmount().divide(new BigDecimal("3"), 2, java.math.RoundingMode.HALF_UP);
        BigDecimal remainder = invoice.getAmount().subtract(oneThird.multiply(new BigDecimal("2")));
        
        InvoiceLineItem item1 = InvoiceLineItem.builder()
                .invoiceId(invoice.getId())
                .description("Mock Item 1")
                .quantity(1)
                .unitPrice(oneThird)
                .total(oneThird)
                .tenantId(tenantId)
                .build();
                
        InvoiceLineItem item2 = InvoiceLineItem.builder()
                .invoiceId(invoice.getId())
                .description("Mock Item 2")
                .quantity(1)
                .unitPrice(oneThird)
                .total(oneThird)
                .tenantId(tenantId)
                .build();
                
        InvoiceLineItem item3 = InvoiceLineItem.builder()
                .invoiceId(invoice.getId())
                .description("Mock Item 3")
                .quantity(1)
                .unitPrice(remainder)
                .total(remainder)
                .tenantId(tenantId)
                .build();

        invoice.setStatus(InvoiceStatus.EXTRACTED);
        invoiceRepository.save(invoice);
        
        invoiceLineItemRepository.save(item1);
        invoiceLineItemRepository.save(item2);
        invoiceLineItemRepository.save(item3);
        
        return List.of(item1, item2, item3);
    }

    @Transactional
    public Invoice approveInvoice(UUID invoiceId, UUID tenantId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
        if (!invoice.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Invoice does not belong to tenant");
        }
        invoice.setStatus(InvoiceStatus.APPROVED);
        return invoiceRepository.save(invoice);
    }

    public List<Invoice> listInvoices(UUID tenantId) {
        return invoiceRepository.findByTenantId(tenantId);
    }

    public List<Invoice> getOverdue(UUID tenantId) {
        return invoiceRepository.findByTenantIdAndDueDateBefore(tenantId, LocalDate.now());
    }
}
