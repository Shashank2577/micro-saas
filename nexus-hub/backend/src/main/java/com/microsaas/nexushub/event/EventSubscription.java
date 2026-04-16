package com.microsaas.nexushub.event;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "event_subscriptions", schema = "tenant")
@Data
@NoArgsConstructor
public class EventSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "subscriber_app", nullable = false)
    private String subscriberApp;

    @Column(name = "event_type_pattern", nullable = false)
    private String eventTypePattern;

    @Column(name = "callback_url", nullable = false)
    private String callbackUrl;

    @Column(name = "secret")
    private String secret;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public boolean matches(String eventType) {
        if (eventTypePattern.equals("*")) return true;
        if (eventTypePattern.endsWith("*")) {
            String prefix = eventTypePattern.substring(0, eventTypePattern.length() - 1);
            return eventType.startsWith(prefix);
        }
        return eventType.equals(eventTypePattern);
    }
}
