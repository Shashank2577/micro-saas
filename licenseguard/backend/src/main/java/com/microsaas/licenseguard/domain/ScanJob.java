package com.microsaas.licenseguard.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "scan_jobs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScanJob {
    @Id
    private UUID id;
    private UUID repositoryId;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private UUID tenantId;
}
