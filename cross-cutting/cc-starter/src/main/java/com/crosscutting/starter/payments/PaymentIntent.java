package com.crosscutting.starter.payments;

import java.util.Map;

public record PaymentIntent(
        String id,
        long amount,
        String currency,
        String status,
        Map<String, String> metadata
) {
}
