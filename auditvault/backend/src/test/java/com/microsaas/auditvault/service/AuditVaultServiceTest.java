package com.microsaas.auditvault.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatResponse;
import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.auditvault.dto.EvidenceIngestRequest;
import com.microsaas.auditvault.dto.PackageGenerateRequest;
import com.microsaas.auditvault.model.AuditPackage;
import com.microsaas.auditvault.model.Control;
import com.microsaas.auditvault.model.Evidence;
import com.microsaas.auditvault.model.EvidenceMapping;
import com.microsaas.auditvault.repository.*;
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
class AuditVaultServiceTest {

    @Mock
    private FrameworkRepository frameworkRepository;
    @Mock
    private ControlRepository controlRepository;
    @Mock
    private EvidenceRepository evidenceRepository;
    @Mock
    private EvidenceMappingRepository mappingRepository;
    @Mock
    private AuditPackageRepository packageRepository;
    @Mock
    private AiService aiService;

    @InjectMocks
    private AuditVaultService auditVaultService;

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
    void testIngestEvidence() {
        EvidenceIngestRequest req = new EvidenceIngestRequest();
        req.setSourceApp("TestApp");
        req.setEvidenceType("LOG");
        req.setContent("Content");
        
        when(evidenceRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Evidence evidence = auditVaultService.ingestEvidence(req);

        assertEquals(tenantId, evidence.getTenantId());
        assertEquals("TestApp", evidence.getSourceApp());
        assertEquals("PENDING_MAPPING", evidence.getStatus());
    }

    @Test
    void testMapEvidence() {
        UUID evidenceId = UUID.randomUUID();
        UUID frameworkId = UUID.randomUUID();

        Evidence evidence = new Evidence();
        evidence.setId(evidenceId);
        evidence.setTenantId(tenantId);
        evidence.setContent("Evidence content");

        Control control = new Control();
        control.setId(UUID.randomUUID());
        control.setControlCode("CC1");
        control.setTitle("Test Control");

        when(evidenceRepository.findById(evidenceId)).thenReturn(Optional.of(evidence));
        when(controlRepository.findByTenantIdAndFrameworkId(tenantId, frameworkId)).thenReturn(List.of(control));
        
        ChatResponse chatResponse = new ChatResponse("id", "model", "Rationale from AI", null);
        when(aiService.chat(any(ChatRequest.class))).thenReturn(chatResponse);

        when(mappingRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        EvidenceMapping mapping = auditVaultService.mapEvidence(evidenceId, frameworkId);

        assertNotNull(mapping);
        assertEquals("Rationale from AI", mapping.getAiRationale());
        assertEquals("AUTO_MAPPED", mapping.getStatus());
        assertEquals("MAPPED", evidence.getStatus());
    }

    @Test
    void testApproveMapping() {
        UUID mappingId = UUID.randomUUID();
        EvidenceMapping mapping = new EvidenceMapping();
        mapping.setId(mappingId);
        mapping.setTenantId(tenantId);
        mapping.setStatus("AUTO_MAPPED");

        when(mappingRepository.findById(mappingId)).thenReturn(Optional.of(mapping));
        when(mappingRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        EvidenceMapping approved = auditVaultService.approveMapping(mappingId);
        assertEquals("APPROVED", approved.getStatus());
    }

    @Test
    void testGeneratePackage() {
        PackageGenerateRequest req = new PackageGenerateRequest();
        req.setFrameworkId(UUID.randomUUID());
        req.setName("Test Package");

        when(packageRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        AuditPackage pkg = auditVaultService.generatePackage(req);
        assertEquals(tenantId, pkg.getTenantId());
        assertEquals("Test Package", pkg.getName());
        assertEquals("READY", pkg.getStatus());
    }
}
