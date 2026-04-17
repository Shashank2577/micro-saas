package com.microsaas.localizationos.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "translation_memories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranslationMemory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private UUID projectId;

    @Column(nullable = false)
    private String sourceLanguage;

    @Column(nullable = false)
    private String targetLanguage;

    @Column(nullable = false)
    private String sourceText;

    @Column(nullable = false)
    private String translatedText;

    @Builder.Default
    @Column(nullable = false)
    private Boolean approved = false;

    @Builder.Default
    @Column(nullable = false)
    private Integer usageCount = 0;
}
