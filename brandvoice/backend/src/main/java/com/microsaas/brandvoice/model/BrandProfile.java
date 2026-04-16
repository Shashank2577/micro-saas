package com.microsaas.brandvoice.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "brand_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String name;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> toneAttributes;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> vocabularyApproved;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> vocabularyBanned;

    @Column(columnDefinition = "text")
    private String styleGuide;

    private BigDecimal consistencyScore;

    @CreationTimestamp
    private ZonedDateTime createdAt;
}
