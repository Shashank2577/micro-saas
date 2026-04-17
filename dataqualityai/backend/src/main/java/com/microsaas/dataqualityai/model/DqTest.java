package com.microsaas.dataqualityai.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "dq_tests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DqTest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String datasetId;

    @Enumerated(EnumType.STRING)
    private TestType type;

    @Column(columnDefinition = "TEXT")
    private String config; // JSON config

    private boolean enabled = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum TestType {
        NOT_NULL, UNIQUE, ACCEPTED_VALUES, RANGE, FK, CUSTOM_SQL, DISTRIBUTION_SHIFT, SCHEMA_DRIFT
    }
}
