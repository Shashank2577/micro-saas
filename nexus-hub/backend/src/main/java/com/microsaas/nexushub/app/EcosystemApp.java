package com.microsaas.nexushub.app;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "ecosystem_apps", schema = "tenant")
@Data
@NoArgsConstructor
public class EcosystemApp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "base_url")
    private String baseUrl;

    @Type(JsonType.class)
    @Column(name = "manifest", columnDefinition = "jsonb")
    private Map<String, Object> manifest;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AppStatus status = AppStatus.ACTIVE;

    @Column(name = "last_heartbeat_at")
    private Instant lastHeartbeatAt;

    @Column(name = "registered_at", nullable = false, updatable = false)
    private Instant registeredAt = Instant.now();

    public enum AppStatus {
        ACTIVE, INACTIVE, ERROR
    }
}
