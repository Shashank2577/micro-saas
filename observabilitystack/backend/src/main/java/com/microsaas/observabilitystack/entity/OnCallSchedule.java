package com.microsaas.observabilitystack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "oncall_schedules")
public class OnCallSchedule {
    @Id
    private UUID id = UUID.randomUUID();
    private String tenantId;
    private String name;
    private String userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
