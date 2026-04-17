package com.crosscutting.event;

import java.util.Map;

public interface EventPublisher {
    void publish(String eventType, Map<String, Object> payload);
}
