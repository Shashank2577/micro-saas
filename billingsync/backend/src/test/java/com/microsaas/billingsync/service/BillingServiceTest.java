package com.microsaas.billingsync.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.billingsync.exception.BadRequestException;
import com.microsaas.billingsync.model.Invoice;
import com.microsaas.billingsync.model.Subscription;
import com.microsaas.billingsync.model.SubscriptionPlan;
import com.microsaas.billingsync.repository.InvoiceRepository;
import com.microsaas.billingsync.repository.PaymentRepository;
import com.microsaas.billingsync.repository.RefundRepository;
import com.microsaas.billingsync.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class BillingServiceTest {

    @InjectMocks
    private BillingService billingService;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private RefundRepository refundRepository;

    @Mock
    private PaymentProcessorService paymentProcessorService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    private String tenantId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        UUID tId = UUID.randomUUID();
        tenantId = tId.toString();
        TenantContext.set(tId);
    }

    @Test
    public void testPayInvoice_Success() {
        UUID invoiceId = UUID.randomUUID();
        Subscription sub = new Subscription();
        sub.setPaymentMethodId("pm_test");

        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);
        invoice.setTenantId(tenantId);
        invoice.setStatus("OPEN");
        invoice.setAmountDue(new BigDecimal("100.00"));
        invoice.setAmountPaid(new BigDecimal("0.00"));
        invoice.setSubscription(sub);

        when(invoiceRepository.findByIdAndTenantId(invoiceId, tenantId))
                .thenReturn(Optional.of(invoice));
        when(paymentProcessorService.processPayment("pm_test", new BigDecimal("100.00"), "USD"))
                .thenReturn(new PaymentProcessorService.PaymentResult(true, "txn_123", null));

        billingService.payInvoice(invoiceId);

        assertEquals("PAID", invoice.getStatus());
        assertEquals(new BigDecimal("100.00"), invoice.getAmountPaid());
        verify(paymentRepository, times(1)).save(any());
        verify(invoiceRepository, times(1)).save(invoice);
    }
    
    @Test
    public void testGetTotalMrr() {
        SubscriptionPlan plan = new SubscriptionPlan();
        plan.setBasePrice(new BigDecimal("50.00"));
        
        Subscription sub = new Subscription();
        sub.setStatus("ACTIVE");
        sub.setPlan(plan);
        
        when(subscriptionRepository.findByTenantId(tenantId)).thenReturn(Collections.singletonList(sub));
        
        BigDecimal mrr = billingService.getTotalMrr();
        assertEquals(new BigDecimal("50.00"), mrr);
    }
}
