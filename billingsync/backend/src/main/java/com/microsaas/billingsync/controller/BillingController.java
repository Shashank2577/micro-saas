package com.microsaas.billingsync.controller;

import com.microsaas.billingsync.model.Invoice;
import com.microsaas.billingsync.model.Payment;
import com.microsaas.billingsync.model.Refund;
import com.microsaas.billingsync.service.BillingService;
import com.microsaas.billingsync.service.LiteLlmClient;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @Autowired
    private LiteLlmClient liteLlmClient;

    @GetMapping("/invoices")
    public List<Invoice> getAllInvoices() {
        return billingService.getAllInvoices();
    }

    @GetMapping("/invoices/{id}")
    public Invoice getInvoiceById(@PathVariable UUID id) {
        return billingService.getInvoiceById(id);
    }

    @PostMapping("/invoices/{id}/pay")
    public Payment payInvoice(@PathVariable UUID id) {
        return billingService.payInvoice(id);
    }

    @PostMapping("/invoices/{id}/refund")
    public Refund refundPayment(@PathVariable UUID id, @RequestBody RefundRequest request) {
        return billingService.refundPayment(id, request.amount, request.reason);
    }

    @GetMapping("/billing/report")
    public Map<String, Object> getBillingReport() {
        Map<String, Object> report = new HashMap<>();
        report.put("totalMrr", billingService.getTotalMrr());
        return report;
    }

    @GetMapping("/billing/recommendations")
    public Map<String, String> getRecommendations() {
        String tenantId = TenantContext.require().toString();
        BigDecimal mrr = billingService.getTotalMrr();
        String recommendation = liteLlmClient.getRevenueOptimizationRecommendations(tenantId, mrr);
        
        Map<String, String> response = new HashMap<>();
        response.put("recommendation", recommendation);
        return response;
    }

    public static class RefundRequest {
        public BigDecimal amount;
        public String reason;
    }
}
