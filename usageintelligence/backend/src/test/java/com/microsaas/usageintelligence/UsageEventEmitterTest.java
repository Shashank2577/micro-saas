package com.microsaas.usageintelligence;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.usageintelligence.service.UsageEventEmitter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsageEventEmitterTest {

    private ApplicationEventPublisher publisher;
    private UsageEventEmitter emitter;

    @BeforeEach
    void setUp() {
        publisher = Mockito.mock(ApplicationEventPublisher.class);
        emitter = new UsageEventEmitter(publisher);
        TenantContext.set(UUID.randomUUID());
    }

    @Test
    void testEmitAnomalyDetected() {
        emitter.emitAnomalyDetected("Test Anomaly", "Fix it");
        
        ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
        Mockito.verify(publisher).publishEvent(captor.capture());
        
        Map event = captor.getValue();
        assertEquals("usage.anomaly_detected", event.get("eventType"));
        assertEquals("Test Anomaly", event.get("title"));
    }
}
