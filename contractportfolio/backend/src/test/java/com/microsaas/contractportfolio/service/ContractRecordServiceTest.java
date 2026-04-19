package com.microsaas.contractportfolio.service;

import com.microsaas.contractportfolio.domain.ContractRecord;
import com.microsaas.contractportfolio.repository.ContractRecordRepository;
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
class ContractRecordServiceTest {

    @Mock
    private ContractRecordRepository repository;

    @InjectMocks
    private ContractRecordService service;

    @Test
    void testCreate() {
        ContractRecord entity = new ContractRecord();
        when(repository.save(any(ContractRecord.class))).thenReturn(entity);
        ContractRecord result = service.create(entity);
        assertNotNull(result);
    }

    @Test
    void testGetById() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(new ContractRecord()));
        Optional<ContractRecord> result = service.getById(id, tenantId);
        assertTrue(result.isPresent());
    }
}
