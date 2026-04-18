package com.microsaas.agencyos.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "projects")
@Getter
@Setter
public class Project extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private String name;
    private String description;
    
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;
}
