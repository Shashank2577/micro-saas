package com.microsaas.dataroomai.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private DataRoom room;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, name = "file_path")
    private String filePath;

    @Column(nullable = false, name = "tenant_id")
    private UUID tenantId;

    @Column(name = "upload_at", insertable = false, updatable = false)
    private ZonedDateTime uploadAt;

    @Column(name = "last_read_at")
    private ZonedDateTime lastReadAt;
}
