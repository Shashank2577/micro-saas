package com.microsaas.goaltracker.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "goal_achievement_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalAchievementRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;

    @Column(name = "achieved_amount", nullable = false)
    private BigDecimal achievedAmount;

    @Column(name = "completion_date", nullable = false)
    private LocalDateTime completionDate;

    @Column(name = "time_to_completion_days", nullable = false)
    private Integer timeToCompletionDays;

    @Column(name = "lessons_learned", columnDefinition = "TEXT")
    private String lessonsLearned;
}
