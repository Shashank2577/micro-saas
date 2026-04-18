package com.microsaas.usageintelligence;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.usageintelligence.domain.Event;
import com.microsaas.usageintelligence.dto.CreateEventDto;
import com.microsaas.usageintelligence.repository.AiInsightRepository;
import com.microsaas.usageintelligence.repository.CohortRepository;
import com.microsaas.usageintelligence.repository.EventRepository;
import com.microsaas.usageintelligence.repository.MetricRepository;
import com.microsaas.usageintelligence.service.UsageIntelligenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UsageIntelligenceServiceTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private MetricRepository metricRepository;
    @Mock
    private AiInsightRepository aiInsightRepository;
    @Mock
    private CohortRepository cohortRepository;

    @InjectMocks
    private UsageIntelligenceService service;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        TenantContext.set(tenantId);
    }

    @Test
    void testTrackEvent() {
        CreateEventDto dto = new CreateEventDto();
        dto.setUserId("user1");
        dto.setEventName("login");
        dto.setProperties(Map.of("platform", "web"));

        Event savedEvent = new Event();
        savedEvent.setId(UUID.randomUUID());
        savedEvent.setTenantId(tenantId);
        savedEvent.setUserId("user1");
        savedEvent.setEventName("login");

        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);

        Event result = service.trackEvent(dto);

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        assertEquals("user1", result.getUserId());
        assertEquals("login", result.getEventName());
    }
}
