package com.microsaas.experimentengine.domain.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "event")
@Data
public class Event {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "experiment_id", nullable = false)
    private UUID experimentId;

    @Column(name = "unit_id", nullable = false)
    private String unitId;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "value")
    private Double value;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private String properties;

    @CreationTimestamp
    @Column(name = "ts", nullable = false, updatable = false)
    private Instant ts;
}
