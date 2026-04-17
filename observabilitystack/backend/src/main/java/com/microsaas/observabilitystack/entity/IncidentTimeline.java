package com.microsaas.observabilitystack.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "incident_timelines")
public class IncidentTimeline {
    @Id
    private UUID id = UUID.randomUUID();
    private String tenantId;

    @ManyToOne
    @JoinColumn(name = "incident_id")
    private Incident incident;

    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();
}
