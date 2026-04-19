package com.microsaas.auditready.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "control")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Control {
    @Id
    private UUID id;
    private UUID tenantId;
    private UUID frameworkId;
    private String name;
    private String description;
    private String status;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
