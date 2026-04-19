package com.microsaas.hiresignal.service;

import com.microsaas.hiresignal.model.CandidateProfile;
import com.microsaas.hiresignal.repository.CandidateProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CandidateProfileServiceTest {

    @Mock
    private CandidateProfileRepository repository;

    @InjectMocks
    private CandidateProfileService service;

    private CandidateProfile candidateProfile;
    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        candidateProfile = new CandidateProfile();
        candidateProfile.setId(UUID.randomUUID());
        candidateProfile.setTenantId(tenantId);
        candidateProfile.setName("John Doe");
        candidateProfile.setStatus("Active");
    }

    @Test
    void testFindById() {
        when(repository.findByIdAndTenantId(candidateProfile.getId(), tenantId)).thenReturn(Optional.of(candidateProfile));
        Optional<CandidateProfile> found = service.findById(candidateProfile.getId(), tenantId);
        assertTrue(found.isPresent());
        assertEquals("John Doe", found.get().getName());
    }

    @Test
    void testSave() {
        when(repository.save(any(CandidateProfile.class))).thenReturn(candidateProfile);
        CandidateProfile saved = service.save(candidateProfile);
        assertNotNull(saved);
        assertEquals("John Doe", saved.getName());
    }

    @Test
    void testValidate() {
        when(repository.findByIdAndTenantId(candidateProfile.getId(), tenantId)).thenReturn(Optional.of(candidateProfile));
        Map<String, Object> response = service.validate(candidateProfile.getId(), tenantId);
        assertTrue((Boolean) response.get("valid"));
    }
}
