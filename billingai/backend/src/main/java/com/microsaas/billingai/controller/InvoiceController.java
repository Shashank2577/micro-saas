package com.microsaas.billingai.controller;

import com.microsaas.billingai.model.Invoice;
import com.microsaas.billingai.model.InvoiceStatus;
import com.microsaas.billingai.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        invoice.setTenantId(tenantId);
        return ResponseEntity.ok(invoiceService.createInvoice(invoice));
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getInvoices(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(invoiceService.getInvoicesByTenant(tenantId));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id, tenantId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Invoice> updateStatus(@PathVariable UUID id, @RequestParam InvoiceStatus status, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(invoiceService.updateInvoiceStatus(id, status, tenantId));
    }
}
