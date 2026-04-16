package com.microsaas.licenseguard.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "repositories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Repository {
    @Id
    private UUID id;
    private String name;
    private String repoUrl;
    private Instant lastScannedAt;
    private UUID tenantId;
}
