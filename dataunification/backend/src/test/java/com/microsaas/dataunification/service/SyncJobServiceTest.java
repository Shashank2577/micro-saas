package com.microsaas.dataunification.service;

import com.microsaas.dataunification.model.SyncJob;
import com.microsaas.dataunification.repository.SyncJobRepository;
import com.crosscutting.starter.tenancy.TenantContext;
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
public class SyncJobServiceTest {
    @Mock
    private SyncJobRepository repository;

    @InjectMocks
    private SyncJobService service;

    private final UUID tenantId = UUID.randomUUID();

    @Test
    public void testFindAll() {
        try (MockedStatic<TenantContext> mocked = mockStatic(TenantContext.class)) {
            mocked.when(TenantContext::require).thenReturn(tenantId);
            when(repository.findByTenantId(tenantId)).thenReturn(List.of(new SyncJob()));
            
            List<SyncJob> result = service.findAll();
            assertFalse(result.isEmpty());
        }
    }

    @Test
    public void testCreate() {
        try (MockedStatic<TenantContext> mocked = mockStatic(TenantContext.class)) {
            mocked.when(TenantContext::require).thenReturn(tenantId);
            
            SyncJob job = new SyncJob();
            job.setType("BATCH");
            
            when(repository.save(any(SyncJob.class))).thenAnswer(i -> i.getArguments()[0]);
            
            SyncJob result = service.create(job);
            assertNotNull(result.getId());
            assertEquals(tenantId, result.getTenantId());
            assertEquals("PENDING", result.getStatus());
        }
    }

    @Test
    public void testRollback() {
        try (MockedStatic<TenantContext> mocked = mockStatic(TenantContext.class)) {
            mocked.when(TenantContext::require).thenReturn(tenantId);
            
            UUID id = UUID.randomUUID();
            SyncJob job = new SyncJob();
            job.setId(id);
            job.setTenantId(tenantId);
            job.setStatus("PENDING");

            when(repository.findById(id)).thenReturn(Optional.of(job));
            when(repository.save(any(SyncJob.class))).thenAnswer(i -> i.getArguments()[0]);

            SyncJob result = service.rollback(id);
            assertEquals("FAILED", result.getStatus());
            assertEquals("Rolled back manually", result.getErrorLog());
        }
    }
}
