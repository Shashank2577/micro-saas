package com.crosscutting.starter.payments;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentEntityTest {

    @Test
    void paymentIntentRecordFields() {
        PaymentIntent intent = new PaymentIntent("pi_123", 5000, "usd", "requires_payment_method", Map.of("key", "val"));

        assertThat(intent.id()).isEqualTo("pi_123");
        assertThat(intent.amount()).isEqualTo(5000);
        assertThat(intent.currency()).isEqualTo("usd");
        assertThat(intent.status()).isEqualTo("requires_payment_method");
        assertThat(intent.metadata()).containsEntry("key", "val");
    }

    @Test
    void subscriptionRecordFields() {
        Instant end = Instant.parse("2026-12-31T00:00:00Z");
        Subscription sub = new Subscription("sub_1", "cus_1", "price_1", "active", end);

        assertThat(sub.id()).isEqualTo("sub_1");
        assertThat(sub.customerId()).isEqualTo("cus_1");
        assertThat(sub.priceId()).isEqualTo("price_1");
        assertThat(sub.status()).isEqualTo("active");
        assertThat(sub.currentPeriodEnd()).isEqualTo(end);
    }

    @Test
    void paymentIntentEquality() {
        Map<String, String> meta = Map.of();
        PaymentIntent a = new PaymentIntent("pi_1", 100, "usd", "ok", meta);
        PaymentIntent b = new PaymentIntent("pi_1", 100, "usd", "ok", meta);
        assertThat(a).isEqualTo(b);
    }

    @Test
    void subscriptionEquality() {
        Instant end = Instant.now();
        Subscription a = new Subscription("s1", "c1", "p1", "active", end);
        Subscription b = new Subscription("s1", "c1", "p1", "active", end);
        assertThat(a).isEqualTo(b);
    }
}
