package com.microsaas.billingsync.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.billingsync.model.Subscription;
import com.microsaas.billingsync.model.SubscriptionPlan;
import com.microsaas.billingsync.repository.SubscriptionPlanRepository;
import com.microsaas.billingsync.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class SubscriptionServiceTest {

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private SubscriptionPlanRepository planRepository;

    @Mock
    private BillingService billingService;

    private String tenantId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        UUID tId = UUID.randomUUID();
        tenantId = tId.toString();
        TenantContext.set(tId);
    }

    @Test
    public void testCreateSubscription() {
        UUID planId = UUID.randomUUID();
        SubscriptionPlan plan = new SubscriptionPlan();
        plan.setBillingPeriod("MONTHLY");

        when(planRepository.findByIdAndTenantId(planId, tenantId)).thenReturn(Optional.of(plan));
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(i -> i.getArguments()[0]);
        when(billingService.generateInvoice(any(), any(), any(), anyBoolean())).thenReturn(null);

        Subscription sub = subscriptionService.createSubscription(planId, "pm_test");

        assertNotNull(sub);
        assertEquals(tenantId, sub.getTenantId());
        assertEquals("ACTIVE", sub.getStatus());
        assertEquals("pm_test", sub.getPaymentMethodId());
        assertNotNull(sub.getCurrentPeriodStart());
        assertNotNull(sub.getCurrentPeriodEnd());
    }
}
