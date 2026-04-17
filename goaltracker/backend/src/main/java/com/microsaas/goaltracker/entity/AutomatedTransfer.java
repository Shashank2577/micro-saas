package com.microsaas.goaltracker.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "automated_transfers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutomatedTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String frequency; // weekly, bi-weekly, monthly

    @Column(name = "source_account_id")
    private String sourceAccountId;

    @Column(name = "destination_account_id")
    private String destinationAccountId;

    @Column(name = "next_run_date")
    private LocalDateTime nextRunDate;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Boolean paused = false;

    @OneToMany(mappedBy = "automatedTransfer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransferHistory> history;
}
