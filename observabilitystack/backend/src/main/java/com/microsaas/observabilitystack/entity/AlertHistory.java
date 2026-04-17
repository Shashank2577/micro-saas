package com.microsaas.observabilitystack.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "alert_history")
public class AlertHistory {
    @Id
    private UUID id = UUID.randomUUID();
    private String tenantId;

    @ManyToOne
    @JoinColumn(name = "alert_id")
    private Alert alert;

    private String status;
    private LocalDateTime timestamp = LocalDateTime.now();
}
