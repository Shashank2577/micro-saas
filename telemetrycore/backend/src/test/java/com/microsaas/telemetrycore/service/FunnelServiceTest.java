package com.microsaas.telemetrycore.service;

import com.microsaas.telemetrycore.model.Funnel;
import com.microsaas.telemetrycore.model.Event;
import com.microsaas.telemetrycore.repository.FunnelRepository;
import com.microsaas.telemetrycore.repository.EventRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FunnelServiceTest {

    @Mock
    private FunnelRepository funnelRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private FunnelService funnelService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void testCreateFunnel_success() {
        Funnel funnel = new Funnel();
        funnel.setName("Signup Funnel");

        when(funnelRepository.save(any(Funnel.class))).thenAnswer(invocation -> {
            Funnel saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        Funnel result = funnelService.createFunnel(funnel);

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        verify(funnelRepository, times(1)).save(any(Funnel.class));
    }

    @Test
    void testAnalyzeFunnel_returnsConversionRates() {
        UUID funnelId = UUID.randomUUID();
        Funnel funnel = new Funnel();
        funnel.setId(funnelId);
        funnel.setSteps(Arrays.asList("Step1", "Step2"));

        when(funnelRepository.findByIdAndTenantId(funnelId, tenantId)).thenReturn(Optional.of(funnel));

        Event e1 = new Event();
        e1.setEventName("Step1");
        e1.setUserId("user1");

        Event e2 = new Event();
        e2.setEventName("Step2");
        e2.setUserId("user1");

        Event e3 = new Event();
        e3.setEventName("Step1");
        e3.setUserId("user2");

        when(eventRepository.findByTenantIdAndTimestampBetween(eq(tenantId), any(ZonedDateTime.class), any(ZonedDateTime.class)))
                .thenReturn(Arrays.asList(e1, e2, e3));

        Map<String, Object> result = funnelService.analyzeFunnel(funnelId);

        assertEquals(100, result.get("Step1"));
        assertEquals(50L, result.get("Step2"));
    }

    @Test
    void testAnalyzeFunnel_withZeroUsers() {
        UUID funnelId = UUID.randomUUID();
        Funnel funnel = new Funnel();
        funnel.setId(funnelId);
        funnel.setSteps(Arrays.asList("Step1", "Step2"));

        when(funnelRepository.findByIdAndTenantId(funnelId, tenantId)).thenReturn(Optional.of(funnel));

        when(eventRepository.findByTenantIdAndTimestampBetween(eq(tenantId), any(ZonedDateTime.class), any(ZonedDateTime.class)))
                .thenReturn(Arrays.asList());

        Map<String, Object> result = funnelService.analyzeFunnel(funnelId);

        assertEquals(0, result.get("Step1"));
        assertEquals(0, result.get("Step2"));
    }
}
