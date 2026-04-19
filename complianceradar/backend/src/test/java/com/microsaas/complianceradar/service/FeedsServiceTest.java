package com.microsaas.complianceradar.service;

import com.microsaas.complianceradar.domain.RegulationUpdate;
import com.microsaas.complianceradar.repository.RegulationUpdateRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedsServiceTest {

    @Mock
    private RegulationUpdateRepository repository;

    @InjectMocks
    private FeedsService service;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testList() {
        when(repository.findAllByTenantId(tenantId)).thenReturn(List.of(new RegulationUpdate()));
        List<RegulationUpdate> result = service.list();
        assertEquals(1, result.size());
    }

    @Test
    void testCreate() {
        RegulationUpdate update = RegulationUpdate.builder().name("Test").build();
        when(repository.save(any(RegulationUpdate.class))).thenAnswer(i -> i.getArguments()[0]);
        RegulationUpdate result = service.create(update);
        assertNotNull(result.getId());
        assertEquals(tenantId, result.getTenantId());
    }

    @Test
    void testGetById() {
        UUID id = UUID.randomUUID();
        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(RegulationUpdate.builder().id(id).build()));
        RegulationUpdate result = service.getById(id);
        assertEquals(id, result.getId());
    }
}
