package com.microsaas.procurebot.service;

import com.microsaas.procurebot.dto.PurchaseRequestRequest;
import com.microsaas.procurebot.model.PurchaseRequest;
import com.microsaas.procurebot.repository.PurchaseRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseRequestServiceTest {

    @Mock
    private PurchaseRequestRepository repository;

    @InjectMocks
    private PurchaseRequestService service;

    @Test
    void testCreate() {
        UUID tenantId = UUID.randomUUID();
        PurchaseRequestRequest req = new PurchaseRequestRequest();
        req.setName("Test Request");
        req.setStatus("DRAFT");

        PurchaseRequest pr = PurchaseRequest.builder()
                .id(UUID.randomUUID())
                .tenantId(tenantId)
                .name("Test Request")
                .status("DRAFT")
                .build();

        when(repository.save(any(PurchaseRequest.class))).thenReturn(pr);

        PurchaseRequest result = service.create(tenantId, req);

        assertNotNull(result);
        assertEquals("Test Request", result.getName());
        assertEquals("DRAFT", result.getStatus());
        assertEquals(tenantId, result.getTenantId());

        verify(repository, times(1)).save(any(PurchaseRequest.class));
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();

        PurchaseRequest pr = PurchaseRequest.builder()
                .id(id)
                .tenantId(tenantId)
                .name("Test Request")
                .status("DRAFT")
                .build();

        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(pr));

        PurchaseRequest result = service.findById(id, tenantId);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(tenantId, result.getTenantId());
    }
}
