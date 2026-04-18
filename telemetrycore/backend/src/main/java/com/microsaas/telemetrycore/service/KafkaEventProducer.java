package com.microsaas.telemetrycore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KafkaEventProducer {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public void emitAlertThresholdBreached(String metricName, double threshold) {
        eventPublisher.publishEvent(Map.of(
                "eventName", "alert_threshold_breached",
                "metric", metricName,
                "threshold", threshold
        ));
    }

    public void emitMetricAnomalyDetected(String metricName, String anomalyDescription) {
        eventPublisher.publishEvent(Map.of(
                "eventName", "metric_anomaly_detected",
                "metric", metricName,
                "description", anomalyDescription
        ));
    }
}
