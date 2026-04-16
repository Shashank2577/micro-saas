package com.microsaas.invoiceprocessor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceProcessorController {

    private final InvoiceService invoiceService;
    private final PoMatchingService poMatchingService;

    public InvoiceProcessorController(InvoiceService invoiceService, PoMatchingService poMatchingService) {
        this.invoiceService = invoiceService;
        this.poMatchingService = poMatchingService;
    }

    // Tenant Header
    private static final String TENANT_HEADER = "X-Tenant-ID";

    @PostMapping
    public ResponseEntity<Invoice> receiveInvoice(@RequestBody Invoice invoice, 
                                                  @RequestHeader(TENANT_HEADER) UUID tenantId) {
        invoice.setTenantId(tenantId);
        return ResponseEntity.ok(invoiceService.receiveInvoice(invoice));
    }

    @PostMapping("/{invoiceId}/extract")
    public ResponseEntity<List<InvoiceLineItem>> extractLineItems(@PathVariable UUID invoiceId,
                                                                  @RequestHeader(TENANT_HEADER) UUID tenantId) {
        return ResponseEntity.ok(invoiceService.extractLineItems(invoiceId, tenantId));
    }

    @PostMapping("/{invoiceId}/approve")
    public ResponseEntity<Invoice> approveInvoice(@PathVariable UUID invoiceId,
                                                  @RequestHeader(TENANT_HEADER) UUID tenantId) {
        return ResponseEntity.ok(invoiceService.approveInvoice(invoiceId, tenantId));
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> listInvoices(@RequestHeader(TENANT_HEADER) UUID tenantId) {
        return ResponseEntity.ok(invoiceService.listInvoices(tenantId));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<Invoice>> getOverdueInvoices(@RequestHeader(TENANT_HEADER) UUID tenantId) {
        return ResponseEntity.ok(invoiceService.getOverdue(tenantId));
    }

    @PostMapping("/po")
    public ResponseEntity<PurchaseOrder> createPo(@RequestBody PurchaseOrder po,
                                                  @RequestHeader(TENANT_HEADER) UUID tenantId) {
        po.setTenantId(tenantId);
        return ResponseEntity.ok(poMatchingService.createPo(po));
    }

    @PostMapping("/{invoiceId}/match")
    public ResponseEntity<InvoiceMatch> matchInvoice(@PathVariable UUID invoiceId,
                                                     @RequestHeader(TENANT_HEADER) UUID tenantId) {
        return ResponseEntity.ok(poMatchingService.matchInvoice(invoiceId, tenantId));
    }

    @GetMapping("/po/unmatched")
    public ResponseEntity<List<PurchaseOrder>> listUnmatchedPos(@RequestHeader(TENANT_HEADER) UUID tenantId) {
        return ResponseEntity.ok(poMatchingService.listUnmatched(tenantId));
    }
}
