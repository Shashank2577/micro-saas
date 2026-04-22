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
@Table(name = "compliance_policies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompliancePolicy {
    @Id
    private UUID id;
    private String name;
    private String description;
    private String allowedLicensesJson;
    private String deniedLicensesJson;
    private UUID tenantId;
}
