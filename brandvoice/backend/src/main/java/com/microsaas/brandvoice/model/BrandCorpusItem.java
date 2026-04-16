package com.microsaas.brandvoice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "brand_corpus_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandCorpusItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private UUID brandProfileId;

    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    private Boolean approved;

    @CreationTimestamp
    private ZonedDateTime addedAt;
}
