package com.microsaas.incidentbrain.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "incidents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;

    private String severity;

    private String status;

    private String rootCauseHypothesis;

    private Double confidenceScore;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<TimelineEvent> timelineEvents;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> similarIncidents; // Storing IDs of similar incidents

    @Column(columnDefinition = "text")
    private String postmortemDraft;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TimelineEvent {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "UTC")
        private Instant timestamp;
        private String source;
        private String type;
        private String summary;
        private String metadata;
    }
}
