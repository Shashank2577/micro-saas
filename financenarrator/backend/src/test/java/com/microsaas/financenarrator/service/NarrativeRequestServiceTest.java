package com.microsaas.financenarrator.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.financenarrator.model.NarrativeRequest;
import com.microsaas.financenarrator.repository.NarrativeRequestRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NarrativeRequestServiceTest {

    @Mock
    private NarrativeRequestRepository repository;

    @Mock
    private EventPublisherService eventPublisher;

    @InjectMocks
    private NarrativeRequestService service;

    private MockedStatic<TenantContext> tenantContextMockedStatic;
    private UUID tenantId;
    private UUID requestId;
    private NarrativeRequest request;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        requestId = UUID.randomUUID();
        tenantContextMockedStatic = Mockito.mockStatic(TenantContext.class);
        tenantContextMockedStatic.when(TenantContext::require).thenReturn(tenantId);

        request = new NarrativeRequest();
        request.setId(requestId);
        request.setTenantId(tenantId);
        request.setName("Test Narrative");
        request.setStatus("DRAFT");
    }

    @AfterEach
    void tearDown() {
        tenantContextMockedStatic.close();
    }

    @Test
    void testCreateNarrativeRequest_success() {
        when(repository.save(any(NarrativeRequest.class))).thenReturn(request);

        NarrativeRequest result = service.create(request);

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        verify(repository, times(1)).save(request);
        verify(eventPublisher, times(1)).publishEvent(
                eq("financenarrator.narrative.generated"),
                eq(tenantId),
                eq("financenarrator"),
                any(Map.class)
        );
    }

    @Test
    void testGetNarrativeRequest_byId_found() {
        when(repository.findByIdAndTenantId(requestId, tenantId)).thenReturn(Optional.of(request));

        NarrativeRequest result = service.getById(requestId);

        assertNotNull(result);
        assertEquals(requestId, result.getId());
        verify(repository, times(1)).findByIdAndTenantId(requestId, tenantId);
    }

    @Test
    void testGetNarrativeRequest_byId_notFound_throwsException() {
        when(repository.findByIdAndTenantId(requestId, tenantId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getById(requestId));
        verify(repository, times(1)).findByIdAndTenantId(requestId, tenantId);
    }

    @Test
    void testListNarrativeRequests_byTenant() {
        when(repository.findByTenantId(tenantId)).thenReturn(List.of(request));

        List<NarrativeRequest> result = service.list();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(repository, times(1)).findByTenantId(tenantId);
    }

    @Test
    void testDeleteNarrativeRequest() {
        when(repository.findByIdAndTenantId(requestId, tenantId)).thenReturn(Optional.of(request));
        doNothing().when(repository).delete(request);

        service.delete(requestId);

        verify(repository, times(1)).findByIdAndTenantId(requestId, tenantId);
        verify(repository, times(1)).delete(request);
    }
}
