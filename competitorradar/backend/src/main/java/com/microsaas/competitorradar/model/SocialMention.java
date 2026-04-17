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
@Table(name = "social_mentions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialMention {
    @Id
    private UUID id;
    private UUID tenantId;
    private UUID competitorId;
    private String platform;
    private String text;
    private String url;
    private BigDecimal sentimentScore;
    private OffsetDateTime postedAt;
}
