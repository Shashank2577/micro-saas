package com.microsaas.logisticsai.controller;

import com.microsaas.logisticsai.domain.LogisticsException;
import com.microsaas.logisticsai.service.ExceptionAgentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExceptionControllerTest {

    @Mock
    private ExceptionAgentService service;

    @InjectMocks
    private ExceptionController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllExceptions() {
        when(service.getAllExceptions()).thenReturn(List.of(new LogisticsException()));
        List<LogisticsException> exceptions = controller.getAllExceptions();
        assertFalse(exceptions.isEmpty());
    }

    @Test
    void reportException() {
        LogisticsException exception = new LogisticsException();
        when(service.reportException(exception)).thenReturn(exception);
        LogisticsException response = controller.reportException(exception);
        assertNotNull(response);
    }

    @Test
    void resolveException() {
        UUID id = UUID.randomUUID();
        when(service.resolveException(id)).thenReturn(new LogisticsException());
        LogisticsException response = controller.resolveException(id);
        assertNotNull(response);
    }
}
