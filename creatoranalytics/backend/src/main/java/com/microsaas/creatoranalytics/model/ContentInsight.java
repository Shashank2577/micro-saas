package com.microsaas.creatoranalytics.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "content_insight")
public class ContentInsight {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private UUID channelId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InsightType insightType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String insightText;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> evidence;

    @Column(nullable = false)
    private LocalDateTime generatedAt;
}
