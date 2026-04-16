package com.microsaas.nexushub.workflow;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "workflow_runs", schema = "tenant")
@Data
@NoArgsConstructor
public class WorkflowRun {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id", nullable = false)
    private Workflow workflow;

    @Column(name = "triggered_at", nullable = false, updatable = false)
    private Instant triggeredAt = Instant.now();

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RunStatus status = RunStatus.RUNNING;

    @Type(JsonType.class)
    @Column(name = "step_results", columnDefinition = "jsonb")
    private Map<String, Object> stepResults;

    @Column(name = "error_message")
    private String errorMessage;

    public enum RunStatus {
        RUNNING, COMPLETED, FAILED
    }
}
