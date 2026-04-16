package com.microsaas.licenseguard.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "license_violations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LicenseViolation {
    @Id
    private UUID id;
    private UUID repositoryId;
    private UUID dependencyId;
    private String violationType;
    private String description;
    private String severity;
    private String status;
    private UUID tenantId;
}
