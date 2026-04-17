package com.microsaas.competitorradar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "hiring_signals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HiringSignal {
    @Id
    private UUID id;
    private UUID tenantId;
    private UUID competitorId;
    private String roleTitle;
    private String department;
    private String location;
    private String source;
    private OffsetDateTime postedAt;
}
