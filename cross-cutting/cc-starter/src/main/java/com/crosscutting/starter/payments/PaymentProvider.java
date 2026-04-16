package com.crosscutting.starter.payments;

import java.util.Map;

public interface PaymentProvider {

    PaymentIntent createPaymentIntent(long amount, String currency, Map<String, String> metadata);

    Subscription createSubscription(String customerId, String priceId);

    void cancelSubscription(String subscriptionId);

    boolean verifyWebhookSignature(String payload, String signature);

    Map<String, Object> handleWebhook(String payload);
}
