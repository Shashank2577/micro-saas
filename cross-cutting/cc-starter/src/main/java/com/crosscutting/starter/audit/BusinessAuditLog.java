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
@Table(name = "business_log", schema = "audit")
@Getter
@Setter
@NoArgsConstructor
public class BusinessAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "action", length = 100, nullable = false)
    private String action;

    @Column(name = "resource_type", length = 100, nullable = false)
    private String resourceType;

    @Column(name = "resource_id")
    private UUID resourceId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "before_state", columnDefinition = "jsonb")
    private Map<String, Object> beforeState = new HashMap<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "after_state", columnDefinition = "jsonb")
    private Map<String, Object> afterState = new HashMap<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "details", columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> details = new HashMap<>();

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "correlation_id")
    private UUID correlationId;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }
}
