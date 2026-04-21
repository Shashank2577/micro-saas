package com.microsaas.securitypulse;

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
@Table(name = "scan_jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScanJob {
    @Id
    private UUID id;
    private String prUrl;
    private String status;
    private Instant createdAt;
    private Instant completedAt;
    private UUID tenantId;
}
