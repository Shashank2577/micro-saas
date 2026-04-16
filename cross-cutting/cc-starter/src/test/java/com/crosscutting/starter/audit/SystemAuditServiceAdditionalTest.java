package com.crosscutting.starter.audit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SystemAuditServiceAdditionalTest {

    @Mock
    private SystemAuditLogRepository repository;

    private SystemAuditService service;

    @BeforeEach
    void setUp() {
        service = new SystemAuditService(repository);
    }

    @Test
    void findByTenant_withNullTenantId_delegatesToFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SystemAuditLog> expected = new PageImpl<>(List.of());

        when(repository.findAll(pageable)).thenReturn(expected);

        Page<SystemAuditLog> result = service.findByTenant(null, pageable);

        assertThat(result.getTotalElements()).isEqualTo(0);
        verify(repository).findAll(pageable);
        verify(repository, never()).findByTenantIdIncludingNull(any(), any());
    }

    @Test
    void findByCorrelation_delegatesToRepository() {
        UUID correlationId = UUID.randomUUID();
        SystemAuditLog log = new SystemAuditLog();
        log.setCorrelationId(correlationId);

        when(repository.findByCorrelationId(correlationId)).thenReturn(List.of(log));

        List<SystemAuditLog> result = service.findByCorrelation(correlationId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCorrelationId()).isEqualTo(correlationId);
    }

    @Test
    void findByUser_returnsPaginatedResults() {
        UUID userId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 5);
        SystemAuditLog log1 = new SystemAuditLog();
        log1.setUserId(userId);
        SystemAuditLog log2 = new SystemAuditLog();
        log2.setUserId(userId);
        Page<SystemAuditLog> expected = new PageImpl<>(List.of(log1, log2), pageable, 2);

        when(repository.findByUserId(userId, pageable)).thenReturn(expected);

        Page<SystemAuditLog> result = service.findByUser(userId, pageable);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).allSatisfy(l ->
                assertThat(l.getUserId()).isEqualTo(userId));
    }

    @Test
    void findByResource_withNoResults_returnsEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SystemAuditLog> empty = new PageImpl<>(List.of());

        when(repository.findByResourceTypeAndResourceId("Widget", "999", pageable))
                .thenReturn(empty);

        Page<SystemAuditLog> result = service.findByResource("Widget", "999", pageable);

        assertThat(result.isEmpty()).isTrue();
    }
}
