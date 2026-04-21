package com.crosscutting.socialintelligence.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SchedulingService {
    private final MetricsSyncService metricsSyncService;

    @Scheduled(fixedRate = 900000)
    public void runSync() {
        metricsSyncService.syncMetrics(UUID.randomUUID());
    }
}
