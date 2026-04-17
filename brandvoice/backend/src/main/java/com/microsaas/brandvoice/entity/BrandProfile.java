package com.microsaas.brandvoice.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "brand_profiles")
@Getter
@Setter
public class BrandProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String tone;

    @Type(JsonBinaryType.class)
    @Column(name = "vocabulary_allowed", columnDefinition = "jsonb")
    private List<String> vocabularyAllowed;

    @Type(JsonBinaryType.class)
    @Column(name = "vocabulary_forbidden", columnDefinition = "jsonb")
    private List<String> vocabularyForbidden;

    @Type(JsonBinaryType.class)
    @Column(name = "core_values", columnDefinition = "jsonb")
    private List<String> coreValues;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
