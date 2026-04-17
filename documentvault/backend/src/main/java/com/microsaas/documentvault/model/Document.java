package com.microsaas.documentvault.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "folder_id")
    private UUID folderId;

    @Column(name = "current_version_id")
    private UUID currentVersionId;

    @Column(nullable = false)
    private String status; // ACTIVE, ARCHIVED, DELETED

    @Column(name = "retention_hold")
    private Boolean retentionHold = false;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "size_bytes", nullable = false)
    private Long sizeBytes;

    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    @Column(name = "encryption_key_id")
    private String encryptionKeyId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_by")
    private UUID updatedBy;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
