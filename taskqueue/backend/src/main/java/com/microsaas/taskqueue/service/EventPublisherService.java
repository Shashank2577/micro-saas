package com.microsaas.taskqueue.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class EventPublisherService {
    private static final Logger log = LoggerFactory.getLogger(EventPublisherService.class);
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    public EventPublisherService(ApplicationEventPublisher eventPublisher, ObjectMapper objectMapper) {
        this.eventPublisher = eventPublisher;
        this.objectMapper = objectMapper;
    }

    public void publishJobCompleted(UUID jobId, String name, String status, String result) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("jobId", jobId.toString());
        payload.put("name", name);
        payload.put("status", status);
        payload.put("result", result);

        log.info("Publishing event taskqueue.job.completed: {}", payload);
        // Using Spring's ApplicationEventPublisher as a simple event bus mechanism in lieu of Kafka/RabbitMQ
        eventPublisher.publishEvent(new TaskQueueEvent("taskqueue.job.completed", payload));
    }

    public void publishJobFailed(UUID jobId, String name, String status, String error) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("jobId", jobId.toString());
        payload.put("name", name);
        payload.put("status", status);
        payload.put("error", error);

        log.info("Publishing event taskqueue.job.failed: {}", payload);
        eventPublisher.publishEvent(new TaskQueueEvent("taskqueue.job.failed", payload));
    }
}
