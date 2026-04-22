package com.microsaas.brandvoice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "content_assets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID tenantId;
    private String title;
    @Column(columnDefinition="TEXT")
    private String content;
    private String type;
    private String status;
    private Double aiAnalysisScore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
