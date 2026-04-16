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
@Table(name = "dependencies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dependency {
    @Id
    private UUID id;
    private UUID repositoryId;
    private String name;
    private String version;
    private String license;
    private UUID tenantId;
}
