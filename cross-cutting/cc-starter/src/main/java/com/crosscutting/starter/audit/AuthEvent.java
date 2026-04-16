package com.crosscutting.starter.audit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "auth_events", schema = "audit")
@Getter
@Setter
@NoArgsConstructor
public class AuthEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "keycloak_event_id", length = 255)
    private String keycloakEventId;

    @Column(name = "event_type", length = 100, nullable = false)
    private String eventType;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "client_id", length = 255)
    private String clientId;

    @Column(name = "session_id", length = 255)
    private String sessionId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "details", columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> details = new HashMap<>();

    @Column(name = "error", length = 255)
    private String error;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }
}
