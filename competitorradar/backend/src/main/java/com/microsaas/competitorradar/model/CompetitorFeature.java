package com.microsaas.competitorradar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.UUID;

@Entity
@Table(name = "competitor_features")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitorFeature {
    @Id
    private UUID id;
    private UUID tenantId;
    private UUID competitorId;
    private UUID featureId;
    private String status;
}
