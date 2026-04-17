package com.microsaas.goaltracker.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.UUID;

@Entity
@Table(name = "goal_sharing")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalSharing {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;

    @Column(name = "shared_with_email", nullable = false)
    private String sharedWithEmail;

    @Column(nullable = false)
    private String permissions; // read-only, etc
}
