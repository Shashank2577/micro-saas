package com.crosscutting.starter.payments;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentProvider paymentProvider;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void createPaymentIntent_delegatesToProvider() {
        Map<String, String> metadata = Map.of("orderId", "123");
        PaymentIntent expected = new PaymentIntent("pi_test", 2000, "usd", "requires_payment_method", metadata);
        when(paymentProvider.createPaymentIntent(2000, "usd", metadata)).thenReturn(expected);

        PaymentIntent result = paymentService.createPaymentIntent(2000, "usd", metadata);

        assertThat(result.id()).isEqualTo("pi_test");
        assertThat(result.amount()).isEqualTo(2000);
        assertThat(result.currency()).isEqualTo("usd");
        verify(paymentProvider).createPaymentIntent(2000, "usd", metadata);
    }

    @Test
    void createSubscription_delegatesToProvider() {
        Subscription expected = new Subscription("sub_test", "cus_123", "price_456", "active", Instant.now());
        when(paymentProvider.createSubscription("cus_123", "price_456")).thenReturn(expected);

        Subscription result = paymentService.createSubscription("cus_123", "price_456");

        assertThat(result.id()).isEqualTo("sub_test");
        assertThat(result.customerId()).isEqualTo("cus_123");
        verify(paymentProvider).createSubscription("cus_123", "price_456");
    }

    @Test
    void cancelSubscription_delegatesToProvider() {
        paymentService.cancelSubscription("sub_123");

        verify(paymentProvider).cancelSubscription("sub_123");
    }

    @Test
    void verifyWebhookSignature_delegatesToProvider() {
        when(paymentProvider.verifyWebhookSignature("payload", "sig")).thenReturn(true);

        boolean result = paymentService.verifyWebhookSignature("payload", "sig");

        assertThat(result).isTrue();
        verify(paymentProvider).verifyWebhookSignature("payload", "sig");
    }

    @Test
    void handleWebhook_delegatesToProvider() {
        Map<String, Object> expected = Map.of("status", "received");
        when(paymentProvider.handleWebhook("payload")).thenReturn(expected);

        Map<String, Object> result = paymentService.handleWebhook("payload");

        assertThat(result).containsEntry("status", "received");
        verify(paymentProvider).handleWebhook("payload");
    }
}
