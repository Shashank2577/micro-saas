package com.microsaas.brandvoice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "vocabulary_lists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VocabularyList {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID tenantId;
    private UUID brandProfileId;
    private String type; // ALLOWED, BANNED
    private String word;
    private String alternative;
}
