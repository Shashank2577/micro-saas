package com.microsaas.competitorradar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "win_loss_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WinLossRecord {
    @Id
    private UUID id;
    private UUID tenantId;
    private UUID competitorId;
    private String outcome;
    private String reason;
    private BigDecimal value;
    private OffsetDateTime date;
}
