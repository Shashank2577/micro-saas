package com.microsaas.documentintelligence.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String filename;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "storage_path", nullable = false, length = 1024)
    private String storagePath;

    @Column(nullable = false)
    private String status;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "size_bytes", nullable = false)
    private Long sizeBytes;

    @CreationTimestamp
    @Column(name = "uploaded_at", updatable = false)
    private ZonedDateTime uploadedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;
}
