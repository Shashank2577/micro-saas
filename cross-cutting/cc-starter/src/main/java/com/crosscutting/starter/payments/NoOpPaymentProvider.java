package com.crosscutting.starter.payments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

public class NoOpPaymentProvider implements PaymentProvider {

    private static final Logger log = LoggerFactory.getLogger(NoOpPaymentProvider.class);

    @Override
    public PaymentIntent createPaymentIntent(long amount, String currency, Map<String, String> metadata) {
        log.warn("NoOpPaymentProvider: createPaymentIntent called (amount={}, currency={})", amount, currency);
        return new PaymentIntent(
                "pi_noop_" + UUID.randomUUID().toString().substring(0, 8),
                amount,
                currency,
                "requires_payment_method",
                metadata != null ? metadata : Map.of()
        );
    }

    @Override
    public Subscription createSubscription(String customerId, String priceId) {
        log.warn("NoOpPaymentProvider: createSubscription called (customer={}, price={})", customerId, priceId);
        return new Subscription(
                "sub_noop_" + UUID.randomUUID().toString().substring(0, 8),
                customerId,
                priceId,
                "active",
                Instant.now().plus(30, ChronoUnit.DAYS)
        );
    }

    @Override
    public void cancelSubscription(String subscriptionId) {
        log.warn("NoOpPaymentProvider: cancelSubscription called (subscriptionId={})", subscriptionId);
    }

    @Override
    public boolean verifyWebhookSignature(String payload, String signature) {
        log.warn("NoOpPaymentProvider: verifyWebhookSignature called — returning true");
        return true;
    }

    @Override
    public Map<String, Object> handleWebhook(String payload) {
        log.warn("NoOpPaymentProvider: handleWebhook called");
        return Map.of("status", "received", "provider", "noop");
    }
}
