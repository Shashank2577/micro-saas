package com.microsaas.careerpath.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.UUID;

@Entity
@Table(name = "career_paths")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareerPath {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_role_id", nullable = false)
    private Role fromRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_role_id", nullable = false)
    private Role toRole;

    @Column(columnDefinition = "TEXT")
    private String description;
}
