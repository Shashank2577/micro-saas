package com.microsaas.nexushub.workflow;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "workflows", schema = "tenant")
@Data
@NoArgsConstructor
public class Workflow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Type(JsonType.class)
    @Column(name = "trigger_condition", columnDefinition = "jsonb")
    private Map<String, Object> triggerCondition;

    @Type(JsonType.class)
    @Column(name = "steps", columnDefinition = "jsonb")
    private List<Map<String, Object>> steps;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "last_run_at")
    private Instant lastRunAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();
}
