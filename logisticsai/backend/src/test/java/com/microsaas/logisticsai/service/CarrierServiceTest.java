package com.microsaas.logisticsai.service;

import com.microsaas.logisticsai.domain.CarrierPerformance;
import com.microsaas.logisticsai.repository.CarrierPerformanceRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CarrierServiceTest {

    @Mock
    private CarrierPerformanceRepository repository;

    @Mock
    private EventPublisherService eventPublisher;

    @InjectMocks
    private CarrierService service;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void getAllCarriers() {
        when(repository.findByTenantId(tenantId)).thenReturn(List.of(new CarrierPerformance()));
        List<CarrierPerformance> carriers = service.getAllCarriers();
        assertFalse(carriers.isEmpty());
    }

    @Test
    void getCarrier() {
        UUID id = UUID.randomUUID();
        CarrierPerformance carrier = new CarrierPerformance();
        carrier.setId(id);
        carrier.setCarrierName("UPS");
        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(carrier));
        
        CarrierPerformance result = service.getCarrier(id);
        assertEquals("UPS", result.getCarrierName());
    }

    @Test
    void addCarrier() {
        CarrierPerformance carrier = new CarrierPerformance();
        carrier.setCarrierName("FedEx");
        when(repository.save(any(CarrierPerformance.class))).thenAnswer(invocation -> {
            CarrierPerformance c = invocation.getArgument(0);
            c.setId(UUID.randomUUID());
            return c;
        });

        CarrierPerformance saved = service.addCarrier(carrier);
        assertEquals("FedEx", saved.getCarrierName());
        assertEquals(95.0, saved.getOnTimeRate());
        assertEquals(10, saved.getPredictedDelayMins());
        
        verify(eventPublisher).publishEvent(eq("carrier.performance.updated"), anyMap());
    }
}
