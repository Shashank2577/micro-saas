package com.microsaas.logisticsai.service;

import com.microsaas.logisticsai.domain.LogisticsException;
import com.microsaas.logisticsai.repository.LogisticsExceptionRepository;
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

class ExceptionAgentServiceTest {

    @Mock
    private LogisticsExceptionRepository repository;

    @Mock
    private EventPublisherService eventPublisher;

    @InjectMocks
    private ExceptionAgentService service;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void getAllExceptions() {
        when(repository.findByTenantId(tenantId)).thenReturn(List.of(new LogisticsException()));
        List<LogisticsException> exceptions = service.getAllExceptions();
        assertFalse(exceptions.isEmpty());
    }

    @Test
    void recoverAiAnalysis() {
        LogisticsException exception = new LogisticsException();
        exception.setDescription("Road closed");
        
        LogisticsException recovered = service.recoverAiAnalysis(new RuntimeException("API failure"), exception);
        
        assertEquals("MEDIUM", recovered.getSeverity());
        assertTrue(recovered.getRecommendedAction().contains("Review manually"));
    }

    @Test
    void resolveException() {
        UUID id = UUID.randomUUID();
        LogisticsException exception = new LogisticsException();
        exception.setId(id);
        exception.setStatus("OPEN");
        
        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(exception));
        when(repository.save(any(LogisticsException.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LogisticsException resolved = service.resolveException(id);
        assertEquals("RESOLVED", resolved.getStatus());
        
        verify(eventPublisher).publishEvent(eq("exception.resolved"), anyMap());
    }
}
