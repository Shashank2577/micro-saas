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
@Table(name = "competitors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Competitor {
    @Id
    private UUID id;
    private UUID tenantId;
    private String name;
    private String website;
    private String industry;
    private String description;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
