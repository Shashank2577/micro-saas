package com.crosscutting.starter.payments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentProvider paymentProvider;

    public PaymentService(PaymentProvider paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    public PaymentIntent createPaymentIntent(long amount, String currency, Map<String, String> metadata) {
        log.info("Creating payment intent: amount={}, currency={}", amount, currency);
        return paymentProvider.createPaymentIntent(amount, currency, metadata);
    }

    public Subscription createSubscription(String customerId, String priceId) {
        log.info("Creating subscription: customer={}, price={}", customerId, priceId);
        return paymentProvider.createSubscription(customerId, priceId);
    }

    public void cancelSubscription(String subscriptionId) {
        log.info("Cancelling subscription: {}", subscriptionId);
        paymentProvider.cancelSubscription(subscriptionId);
    }

    public boolean verifyWebhookSignature(String payload, String signature) {
        return paymentProvider.verifyWebhookSignature(payload, signature);
    }

    public Map<String, Object> handleWebhook(String payload) {
        log.info("Handling payment webhook");
        return paymentProvider.handleWebhook(payload);
    }
}
