package com.microsaas.brandvoice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "brand_guidelines")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandGuideline {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID tenantId;
    private UUID brandProfileId;
    private String category;
    private String rule;
    private LocalDateTime createdAt;
}
