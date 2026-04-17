package com.microsaas.billingai.service;

import com.microsaas.billingai.model.Invoice;
import com.microsaas.billingai.model.InvoiceStatus;
import com.microsaas.billingai.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Transactional
    public Invoice createInvoice(Invoice invoice) {
        invoice.setStatus(InvoiceStatus.DRAFT);
        return invoiceRepository.save(invoice);
    }

    public List<Invoice> getInvoicesByTenant(UUID tenantId) {
        return invoiceRepository.findByTenantId(tenantId);
    }
    
    public Invoice getInvoiceById(UUID id, UUID tenantId) {
        return invoiceRepository.findById(id)
                .filter(i -> i.getTenantId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }

    @Transactional
    public Invoice updateInvoiceStatus(UUID id, InvoiceStatus status, UUID tenantId) {
        Invoice invoice = getInvoiceById(id, tenantId);
        invoice.setStatus(status);
        return invoiceRepository.save(invoice);
    }
}
