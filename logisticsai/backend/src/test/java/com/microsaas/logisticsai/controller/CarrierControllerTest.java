package com.microsaas.logisticsai.controller;

import com.microsaas.logisticsai.domain.CarrierPerformance;
import com.microsaas.logisticsai.service.CarrierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarrierControllerTest {

    @Mock
    private CarrierService service;

    @InjectMocks
    private CarrierController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCarriers() {
        when(service.getAllCarriers()).thenReturn(List.of(new CarrierPerformance()));
        List<CarrierPerformance> carriers = controller.getAllCarriers();
        assertFalse(carriers.isEmpty());
    }

    @Test
    void getCarrier() {
        UUID id = UUID.randomUUID();
        when(service.getCarrier(id)).thenReturn(new CarrierPerformance());
        ResponseEntity<CarrierPerformance> response = controller.getCarrier(id);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void addCarrier() {
        CarrierPerformance carrier = new CarrierPerformance();
        when(service.addCarrier(carrier)).thenReturn(carrier);
        CarrierPerformance response = controller.addCarrier(carrier);
        assertNotNull(response);
    }
}
