package com.microsaas.contractportfolio.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "renewal_alerts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RenewalAlert {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "contract_id", nullable = false)
    private UUID contractId;

    @Column(name = "alert_date", nullable = false)
    private LocalDate alertDate;

    @Column(name = "days_until_renewal", nullable = false)
    private int daysUntilRenewal;

    @Column(name = "acknowledged_by")
    private String acknowledgedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
