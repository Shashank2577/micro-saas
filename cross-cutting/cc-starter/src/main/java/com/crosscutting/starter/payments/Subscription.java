package com.crosscutting.starter.payments;

import java.time.Instant;

public record Subscription(
        String id,
        String customerId,
        String priceId,
        String status,
        Instant currentPeriodEnd
) {
}
