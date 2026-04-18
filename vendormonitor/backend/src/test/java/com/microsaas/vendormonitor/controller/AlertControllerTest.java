package com.microsaas.vendormonitor.controller;

import com.microsaas.vendormonitor.domain.Alert;
import com.microsaas.vendormonitor.service.AlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AlertControllerTest {

    @Mock
    private AlertService alertService;

    @InjectMocks
    private AlertController alertController;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void getOpenAlerts() {
        when(alertService.getOpenAlerts()).thenReturn(List.of(new Alert()));
        ResponseEntity<List<Alert>> response = alertController.getOpenAlerts();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
