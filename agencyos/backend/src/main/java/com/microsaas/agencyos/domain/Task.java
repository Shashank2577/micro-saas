package com.microsaas.agencyos.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class Task extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;
}
