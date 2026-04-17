package com.microsaas.documentintelligence.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "document_extractions")
public class DocumentExtraction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "document_id", nullable = false)
    private UUID documentId;

    @Column(name = "extraction_type", nullable = false)
    private String extractionType;

    @Column(name = "key_name", nullable = false)
    private String keyName;

    @Column(name = "value_text")
    private String valueText;

    @Column(name = "confidence_score")
    private BigDecimal confidenceScore;

    @Column(name = "page_number")
    private Integer pageNumber;

    @Column(name = "bounding_box", columnDefinition = "jsonb")
    private String boundingBox;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;
}
