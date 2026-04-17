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
@Table(name = "contributions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contribution {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "source_account_id")
    private String sourceAccountId;

    @Column(name = "destination_account_id")
    private String destinationAccountId;

    @Column(name = "contribution_date", nullable = false)
    private LocalDateTime contributionDate;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String type; // manual, automated, windfall
}
