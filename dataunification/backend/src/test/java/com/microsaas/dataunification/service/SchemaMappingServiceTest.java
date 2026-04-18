package com.microsaas.dataunification.service;

import com.microsaas.dataunification.model.SchemaMapping;
import com.microsaas.dataunification.repository.SchemaMappingRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SchemaMappingServiceTest {
    @Mock
    private SchemaMappingRepository repository;

    @InjectMocks
    private SchemaMappingService service;

    private final UUID tenantId = UUID.randomUUID();

    @Test
    public void testFindAll() {
        try (MockedStatic<TenantContext> mocked = mockStatic(TenantContext.class)) {
            mocked.when(TenantContext::require).thenReturn(tenantId);
            when(repository.findByTenantId(tenantId)).thenReturn(List.of(new SchemaMapping()));
            
            List<SchemaMapping> result = service.findAll();
            assertFalse(result.isEmpty());
        }
    }

    @Test
    public void testCreate() {
        try (MockedStatic<TenantContext> mocked = mockStatic(TenantContext.class)) {
            mocked.when(TenantContext::require).thenReturn(tenantId);
            
            SchemaMapping map = new SchemaMapping();
            map.setName("Test");
            
            when(repository.save(any(SchemaMapping.class))).thenAnswer(i -> i.getArguments()[0]);
            
            SchemaMapping result = service.create(map);
            assertNotNull(result.getId());
            assertEquals(tenantId, result.getTenantId());
            assertEquals("Test", result.getName());
        }
    }
}
