package com.microsaas.creatoranalytics.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "content_performance")
public class ContentPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private UUID channelId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contentTitle;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contentUrl;

    @Column(nullable = false)
    private Long views;

    @Column(nullable = false)
    private Long clicks;

    @Column(nullable = false)
    private Integer watchTimeMinutes;

    @Column(nullable = false)
    private LocalDate measuredAt;
}
