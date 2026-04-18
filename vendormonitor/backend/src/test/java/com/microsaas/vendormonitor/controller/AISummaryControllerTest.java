package com.microsaas.vendormonitor.controller;

import com.microsaas.vendormonitor.domain.RenewalSummary;
import com.microsaas.vendormonitor.service.AISummaryService;
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

class AISummaryControllerTest {

    @Mock
    private AISummaryService aiSummaryService;

    @InjectMocks
    private AISummaryController aiSummaryController;

    private AutoCloseable closeable;
    private final UUID vendorId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSummariesForVendor() {
        when(aiSummaryService.getSummariesForVendor(vendorId)).thenReturn(List.of(new RenewalSummary()));
        ResponseEntity<List<RenewalSummary>> response = aiSummaryController.getSummariesForVendor(vendorId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
