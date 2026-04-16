package com.microsaas.nexushub.event;

import java.util.Map;

public record PublishEventRequest(
        String sourceApp,
        String eventType,
        Map<String, Object> payload
) {}
