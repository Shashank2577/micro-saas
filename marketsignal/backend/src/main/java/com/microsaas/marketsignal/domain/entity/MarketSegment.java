package com.microsaas.marketsignal.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "market_segment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String name;

    private String description;

    // Use a basic element collection or converter for String[] if necessary,
    // but a List mapped to an array column can be tricky in standard JPA.
    // We'll use converter or element collection. Since we used keywords TEXT[]
    // we can just map it as List<String> with a converter.
    // For simplicity, let's use a delimited string or standard Hibernate types if possible.
    // Actually, Hibernate 6 handles List mapped to arrays automatically for Postgres array types.
    @Column(columnDefinition = "text[]")
    private List<String> keywords;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
