package com.micro.interviewos.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class InterviewEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void emitScoreSubmitted(UUID tenantId, Map<String, Object> payload) {
        applicationEventPublisher.publishEvent(new InterviewEvent("interviewos.score.submitted", tenantId, payload));
    }

    public void emitCalibrationRequired(UUID tenantId, Map<String, Object> payload) {
        applicationEventPublisher.publishEvent(new InterviewEvent("interviewos.calibration.required", tenantId, payload));
    }

    public void emitPacketReady(UUID tenantId, Map<String, Object> payload) {
        applicationEventPublisher.publishEvent(new InterviewEvent("interviewos.packet.ready", tenantId, payload));
    }
}
