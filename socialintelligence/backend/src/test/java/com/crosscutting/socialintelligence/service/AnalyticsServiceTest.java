package com.crosscutting.socialintelligence.service;

import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnalyticsServiceTest {

    @Test
    void testGetDashboardMetrics() {
        AnalyticsService service = new AnalyticsService();
        Map<String, Object> metrics = service.getDashboardMetrics(UUID.randomUUID());
        assertEquals(10000, metrics.get("totalFollowers"));
        assertEquals(4.5, metrics.get("avgEngagementRate"));
    }
}
