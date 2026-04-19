package com.microsaas.debtnavigator.service;

import com.microsaas.debtnavigator.entity.DebtAccount;
import com.microsaas.debtnavigator.repository.DebtAccountRepository;
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
class AccountsServiceTest {

    @Mock
    private DebtAccountRepository repository;

    @InjectMocks
    private AccountsService service;

    @Test
    void testCreate() {
        DebtAccount account = new DebtAccount();
        when(repository.save(any(DebtAccount.class))).thenReturn(account);
        assertNotNull(service.create(new DebtAccount()));
        verify(repository).save(any(DebtAccount.class));
    }

    @Test
    void testGetById() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(new DebtAccount()));
        assertTrue(service.getById(id, tenantId).isPresent());
    }
}
