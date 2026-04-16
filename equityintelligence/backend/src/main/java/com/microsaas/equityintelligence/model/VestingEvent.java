package com.microsaas.equityintelligence.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "vesting_event")
public class VestingEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "grant_id", nullable = false)
    private UUID grantId;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private LocalDate vestDate;

    @Column(nullable = false)
    private Long sharesVested;

    @Column(nullable = false)
    private Long cumulativeVested;
}
