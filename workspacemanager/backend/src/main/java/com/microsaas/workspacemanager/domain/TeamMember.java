package com.microsaas.workspacemanager.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "team_members")
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "team_id", nullable = false)
    private UUID teamId;

    @Column(name = "member_id", nullable = false)
    private UUID memberId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public UUID getId() { return this.id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getTeamId() { return this.teamId; }
    public void setTeamId(UUID teamId) { this.teamId = teamId; }

    public UUID getMemberId() { return this.memberId; }
    public void setMemberId(UUID memberId) { this.memberId = memberId; }

    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public TeamMember() {}
}
