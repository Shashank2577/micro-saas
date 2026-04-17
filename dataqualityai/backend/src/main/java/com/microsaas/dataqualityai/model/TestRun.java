package com.microsaas.dataqualityai.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "dq_test_runs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestRun {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String testId;

    private LocalDateTime executedAt;

    @Enumerated(EnumType.STRING)
    private RunStatus status;

    @Column(columnDefinition = "TEXT")
    private String observedJson;

    @Column(columnDefinition = "TEXT")
    private String expectedJson;

    public enum RunStatus {
        PASS, FAIL, ERROR
    }
}
