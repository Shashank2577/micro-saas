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
@Table(name = "license_obligations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LicenseObligation {
    @Id
    private UUID id;
    private UUID licenseId;
    private String obligationType;
    private String description;
    private UUID tenantId;
}
