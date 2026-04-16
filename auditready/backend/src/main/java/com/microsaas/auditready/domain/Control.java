package com.microsaas.auditready.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "controls")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Control {
    @Id
    private UUID id;
    private UUID frameworkId;
    private String controlId;
    private String title;
    private String description;
    private String evidenceRequirements;
    private UUID tenantId;
}
