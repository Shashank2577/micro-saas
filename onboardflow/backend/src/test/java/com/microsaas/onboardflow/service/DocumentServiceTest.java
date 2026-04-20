package com.microsaas.onboardflow.service;

import com.microsaas.onboardflow.dto.DocumentRequest;
import com.microsaas.onboardflow.model.Document;
import com.microsaas.onboardflow.repository.DocumentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {

    @Mock
    private DocumentRepository repository;

    @InjectMocks
    private DocumentService service;

    @Test
    public void testFindAll() {
        UUID tenantId = UUID.randomUUID();
        when(repository.findByTenantId(tenantId)).thenReturn(Collections.emptyList());

        assertTrue(service.findAll(tenantId).isEmpty());
        verify(repository, times(1)).findByTenantId(tenantId);
    }

    @Test
    public void testCreate() {
        UUID tenantId = UUID.randomUUID();
        DocumentRequest request = new DocumentRequest();
        request.setName("Test");

        Document savedEntity = new Document();
        savedEntity.setName("Test");

        when(repository.save(any(Document.class))).thenReturn(savedEntity);

        Document result = service.create(tenantId, request);
        assertEquals("Test", result.getName());
    }

    @Test
    public void testFindById_NotFound() {
        UUID tenantId = UUID.randomUUID();
        UUID id = UUID.randomUUID();

        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.findById(id, tenantId));
    }
}
