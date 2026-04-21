package com.microsaas.contextlayer.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.contextlayer.domain.ConsentRecord;
import com.microsaas.contextlayer.repository.ConsentRecordRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrivacyEnforcementServiceTest {
    @Mock
    private ConsentRecordRepository repository;
    @InjectMocks
    private PrivacyEnforcementService service;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() { TenantContext.set(tenantId); }

    @AfterEach
    void tearDown() { TenantContext.clear(); }

    @Test
    void testRecordConsent() {
        ConsentRecord record = new ConsentRecord();
        when(repository.findByCustomerIdAndTenantIdAndConsentType(eq("c1"), eq(tenantId), eq("marketing")))
            .thenReturn(Optional.of(record));
        when(repository.save(any())).thenReturn(record);

        service.recordConsent("c1", "marketing", true, "app1");

        verify(repository).save(any());
        assertTrue(record.getGranted());
    }
}
