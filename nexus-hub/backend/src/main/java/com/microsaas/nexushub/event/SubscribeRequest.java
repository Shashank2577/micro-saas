package com.microsaas.nexushub.event;

public record SubscribeRequest(
        String subscriberApp,
        String eventTypePattern,
        String callbackUrl,
        String secret
) {}
