package com.microsaas.dataunification.service;

import com.microsaas.dataunification.model.DataSource;
import com.microsaas.dataunification.repository.DataSourceRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataSourceServiceTest {
    @Mock
    private DataSourceRepository repository;

    @InjectMocks
    private DataSourceService service;

    private final UUID tenantId = UUID.randomUUID();

    @Test
    public void testFindAll() {
        try (MockedStatic<TenantContext> mocked = mockStatic(TenantContext.class)) {
            mocked.when(TenantContext::require).thenReturn(tenantId);
            when(repository.findByTenantId(tenantId)).thenReturn(List.of(new DataSource()));
            
            List<DataSource> result = service.findAll();
            assertFalse(result.isEmpty());
        }
    }

    @Test
    public void testCreate() {
        try (MockedStatic<TenantContext> mocked = mockStatic(TenantContext.class)) {
            mocked.when(TenantContext::require).thenReturn(tenantId);
            
            DataSource ds = new DataSource();
            ds.setName("Test");
            ds.setType("API");
            
            when(repository.save(any(DataSource.class))).thenAnswer(i -> i.getArguments()[0]);
            
            DataSource result = service.create(ds);
            assertNotNull(result.getId());
            assertEquals(tenantId, result.getTenantId());
            assertEquals("Test", result.getName());
        }
    }
}
