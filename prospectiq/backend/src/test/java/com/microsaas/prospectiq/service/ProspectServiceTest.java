package com.microsaas.prospectiq.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.prospectiq.dto.ProspectRequest;
import com.microsaas.prospectiq.model.Prospect;
import com.microsaas.prospectiq.repository.ProspectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProspectServiceTest {

    @Mock
    private ProspectRepository prospectRepository;

    @InjectMocks
    private ProspectService prospectService;

    private UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        TenantContext.set(tenantId);
    }

    @Test
    void testCreateProspect() {
        ProspectRequest request = new ProspectRequest();
        request.setName("Acme Corp");
        request.setDomain("acme.com");

        Prospect saved = new Prospect();
        saved.setId(UUID.randomUUID());
        saved.setName("Acme Corp");
        saved.setTenantId(tenantId);

        when(prospectRepository.save(any(Prospect.class))).thenReturn(saved);

        Prospect result = prospectService.createProspect(request);

        assertNotNull(result);
        assertEquals("Acme Corp", result.getName());
        assertEquals(tenantId, result.getTenantId());
    }

    @Test
    void testGetAllProspects() {
        when(prospectRepository.findByTenantId(tenantId)).thenReturn(List.of(new Prospect()));

        List<Prospect> list = prospectService.getAllProspects(null, null);

        assertFalse(list.isEmpty());
    }

    @Test
    void testGetProspect() {
        UUID id = UUID.randomUUID();
        when(prospectRepository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(new Prospect()));

        Prospect p = prospectService.getProspect(id);

        assertNotNull(p);
    }
}
