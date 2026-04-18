package com.microsaas.billingsync.service;

import com.microsaas.billingsync.exception.ResourceNotFoundException;
import com.microsaas.billingsync.exception.BadRequestException;
import com.microsaas.billingsync.model.*;
import com.microsaas.billingsync.repository.*;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BillingService {

    @Autowired
    private InvoiceRepository invoiceRepository;
    
    @Autowired
    private InvoiceLineItemRepository lineItemRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private RefundRepository refundRepository;
    
    @Autowired
    private PaymentProcessorService paymentProcessorService;
    
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    
    @Autowired
    private PricingModelRepository pricingModelRepository;
    
    @Autowired
    private MeterEventRepository meterEventRepository;

    private static final BigDecimal TAX_RATE = new BigDecimal("0.08"); // 8% Default

    public List<Invoice> getAllInvoices() {
        String tenantId = TenantContext.require().toString();
        return invoiceRepository.findByTenantId(tenantId);
    }

    public Invoice getInvoiceById(UUID id) {
        String tenantId = TenantContext.require().toString();
        return invoiceRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
    }

    @Transactional
    public Invoice generateInvoice(UUID subscriptionId, LocalDateTime start, LocalDateTime end, boolean includeBasePrice) {
        String tenantId = TenantContext.require().toString();
        Subscription sub = subscriptionRepository.findByIdAndTenantId(subscriptionId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

        BigDecimal subtotal = BigDecimal.ZERO;
        
        Invoice invoice = Invoice.builder()
                .tenantId(tenantId)
                .subscription(sub)
                .amountDue(BigDecimal.ZERO)
                .amountPaid(BigDecimal.ZERO)
                .taxAmount(BigDecimal.ZERO)
                .status("DRAFT")
                .dueDate(LocalDateTime.now().plusDays(15))
                .build();
                
        invoice = invoiceRepository.save(invoice);

        if (includeBasePrice) {
            BigDecimal base = sub.getPlan().getBasePrice();
            subtotal = subtotal.add(base);
            
            InvoiceLineItem item = InvoiceLineItem.builder()
                    .tenantId(tenantId)
                    .invoice(invoice)
                    .description("Base Plan Charge: " + sub.getPlan().getName())
                    .amount(base)
                    .build();
            lineItemRepository.save(item);
        }

        // Calculate overages
        List<PricingModel> models = pricingModelRepository.findByPlanIdAndTenantId(sub.getPlan().getId(), tenantId);
        for (PricingModel model : models) {
            Integer usage = meterEventRepository.sumQuantityBySubscriptionAndMetricAndPeriod(
                tenantId, sub.getId(), model.getMetricName(), start, end);
            
            if (usage == null) usage = 0;
            
            if (usage > model.getIncludedUnits()) {
                int overage = usage - model.getIncludedUnits();
                BigDecimal overageCharge = model.getUnitPrice().multiply(new BigDecimal(overage));
                subtotal = subtotal.add(overageCharge);
                
                InvoiceLineItem item = InvoiceLineItem.builder()
                        .tenantId(tenantId)
                        .invoice(invoice)
                        .description("Overage: " + overage + " " + model.getMetricName())
                        .amount(overageCharge)
                        .build();
                lineItemRepository.save(item);
            }
        }
        
        BigDecimal tax = subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalDue = subtotal.add(tax);
        
        invoice.setTaxAmount(tax);
        invoice.setAmountDue(totalDue);
        invoice.setStatus(totalDue.compareTo(BigDecimal.ZERO) > 0 ? "OPEN" : "PAID");
        
        return invoiceRepository.save(invoice);
    }
    
    @Transactional
    public Invoice generateProrationInvoice(UUID subscriptionId, BigDecimal newPlanCharge, BigDecimal unusedOldPlanCredit) {
        String tenantId = TenantContext.require().toString();
        Subscription sub = subscriptionRepository.findByIdAndTenantId(subscriptionId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

        BigDecimal subtotal = newPlanCharge.subtract(unusedOldPlanCredit);
        if (subtotal.compareTo(BigDecimal.ZERO) < 0) {
            subtotal = BigDecimal.ZERO; // In a real system we'd carry credit forward
        }
        
        Invoice invoice = Invoice.builder()
                .tenantId(tenantId)
                .subscription(sub)
                .amountDue(BigDecimal.ZERO)
                .amountPaid(BigDecimal.ZERO)
                .taxAmount(BigDecimal.ZERO)
                .status("DRAFT")
                .dueDate(LocalDateTime.now().plusDays(15))
                .build();
                
        invoice = invoiceRepository.save(invoice);

        lineItemRepository.save(InvoiceLineItem.builder()
                .tenantId(tenantId)
                .invoice(invoice)
                .description("Prorated charge for new plan")
                .amount(newPlanCharge)
                .build());
                
        lineItemRepository.save(InvoiceLineItem.builder()
                .tenantId(tenantId)
                .invoice(invoice)
                .description("Credit for unused time on old plan")
                .amount(unusedOldPlanCredit.negate())
                .build());
                
        BigDecimal tax = subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalDue = subtotal.add(tax);
        
        invoice.setTaxAmount(tax);
        invoice.setAmountDue(totalDue);
        invoice.setStatus(totalDue.compareTo(BigDecimal.ZERO) > 0 ? "OPEN" : "PAID");
        
        return invoiceRepository.save(invoice);
    }

    @Transactional
    public Payment payInvoice(UUID invoiceId) {
        String tenantId = TenantContext.require().toString();
        Invoice invoice = getInvoiceById(invoiceId);
        
        if (!"DRAFT".equals(invoice.getStatus()) && !"OPEN".equals(invoice.getStatus())) {
            throw new BadRequestException("Invoice cannot be paid in its current status");
        }
        
        BigDecimal amountToPay = invoice.getAmountDue().subtract(invoice.getAmountPaid());
        if (amountToPay.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Invoice is already fully paid");
        }
        
        String pmId = invoice.getSubscription().getPaymentMethodId() != null ? invoice.getSubscription().getPaymentMethodId() : "pm_default";
        
        PaymentProcessorService.PaymentResult result = paymentProcessorService.processPayment(pmId, amountToPay, "USD");
        
        Payment payment = Payment.builder()
                .tenantId(tenantId)
                .invoice(invoice)
                .amount(amountToPay)
                .status(result.success ? "SUCCEEDED" : "FAILED")
                .processorTransactionId(result.transactionId)
                .errorMessage(result.errorMessage)
                .retryCount(result.success ? 0 : 1)
                .build();
                
        paymentRepository.save(payment);
        
        if (result.success) {
            invoice.setAmountPaid(invoice.getAmountPaid().add(amountToPay));
            if (invoice.getAmountPaid().compareTo(invoice.getAmountDue()) >= 0) {
                invoice.setStatus("PAID");
            }
        } else {
             // Initiate dunning management (simple logic)
             System.out.println("Payment failed. Dunning process initiated for invoice: " + invoice.getId());
        }
        invoiceRepository.save(invoice);
        
        return payment;
    }

    @Transactional
    public Refund refundPayment(UUID invoiceId, BigDecimal amount, String reason) {
        String tenantId = TenantContext.require().toString();
        Invoice invoice = getInvoiceById(invoiceId);
        
        List<Payment> payments = paymentRepository.findByTenantId(tenantId).stream()
                .filter(p -> p.getInvoice().getId().equals(invoiceId) && "SUCCEEDED".equals(p.getStatus()))
                .collect(Collectors.toList());
                
        if (payments.isEmpty()) {
            throw new BadRequestException("No successful payments found to refund");
        }
        
        Payment payment = payments.get(0);
        
        PaymentProcessorService.RefundResult result = paymentProcessorService.processRefund(payment.getProcessorTransactionId(), amount);
        
        if (!result.success) {
            throw new BadRequestException("Refund failed: " + result.errorMessage);
        }
        
        Refund refund = Refund.builder()
                .tenantId(tenantId)
                .payment(payment)
                .amount(amount)
                .reason(reason)
                .build();
                
        invoice.setAmountPaid(invoice.getAmountPaid().subtract(amount));
        if (invoice.getAmountPaid().compareTo(invoice.getAmountDue()) < 0) {
            invoice.setStatus("OPEN");
        }
        invoiceRepository.save(invoice);
        
        return refundRepository.save(refund);
    }
    
    public BigDecimal getTotalMrr() {
        String tenantId = TenantContext.require().toString();
        List<Subscription> activeSubs = subscriptionRepository.findByTenantId(tenantId).stream()
            .filter(s -> "ACTIVE".equals(s.getStatus()))
            .collect(Collectors.toList());
            
        return activeSubs.stream()
            .map(s -> s.getPlan().getBasePrice())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
