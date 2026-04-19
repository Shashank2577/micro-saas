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
@Table(name = "evidence")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evidence {
    @Id
    private UUID id;
    private UUID tenantId;
    private UUID controlId;
    private String name;
    private String description;
    private String fileUrl;
    private ZonedDateTime collectedAt;
    private String status;
}
