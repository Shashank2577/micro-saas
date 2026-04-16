package com.microsaas.performancenarrative.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Table(name = "peer_feedback", schema = "performancenarrative")
@Data
public class PeerFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID reviewId;
    private UUID fromEmployeeId;
    
    @Column(columnDefinition = "TEXT")
    private String feedbackText;
    private int rating;
    private UUID tenantId;
}
