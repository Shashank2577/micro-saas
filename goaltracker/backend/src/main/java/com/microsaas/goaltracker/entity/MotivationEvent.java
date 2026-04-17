package com.microsaas.goaltracker.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "motivation_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MotivationEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;

    @Column(name = "nudge_message", nullable = false, columnDefinition = "TEXT")
    private String nudgeMessage;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;
}
