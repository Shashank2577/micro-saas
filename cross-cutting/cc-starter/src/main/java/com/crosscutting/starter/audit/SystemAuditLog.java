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
@Table(name = "system_log", schema = "audit")
@Getter
@Setter
@NoArgsConstructor
public class SystemAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "event_type", length = 50, nullable = false)
    private String eventType;

    @Column(name = "action", length = 100, nullable = false)
    private String action;

    @Column(name = "resource_type", length = 100)
    private String resourceType;

    @Column(name = "resource_id", length = 255)
    private String resourceId;

    @Column(name = "request_method", length = 10)
    private String requestMethod;

    @Column(name = "request_path", columnDefinition = "TEXT")
    private String requestPath;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "request_body", columnDefinition = "jsonb")
    private Map<String, Object> requestBody;

    @Column(name = "response_status")
    private Integer responseStatus;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "details", columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> details = new HashMap<>();

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "correlation_id")
    private UUID correlationId;

    @Column(name = "duration_ms")
    private Integer durationMs;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }
}
