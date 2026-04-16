package com.microsaas.auditready.service;

import com.microsaas.auditready.domain.AuditReadinessScore;
import com.microsaas.auditready.domain.Control;
import com.microsaas.auditready.domain.EvidenceItem;
import com.microsaas.auditready.repository.AuditReadinessScoreRepository;
import com.microsaas.auditready.repository.ControlRepository;
import com.microsaas.auditready.repository.EvidenceItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReadinessScoreServiceTest {

    @Mock
    private AuditReadinessScoreRepository scoreRepository;

    @Mock
    private ControlRepository controlRepository;

    @Mock
    private EvidenceItemRepository evidenceItemRepository;

    @InjectMocks
    private ReadinessScoreService readinessScoreService;

    private UUID tenantId;
    private UUID frameworkId;
    private UUID control1Id;
    private UUID control2Id;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        frameworkId = UUID.randomUUID();
        control1Id = UUID.randomUUID();
        control2Id = UUID.randomUUID();
    }

    @Test
    void scoreIsZeroWithNoEvidence() {
        // Arrange
        Control c1 = Control.builder().id(control1Id).frameworkId(frameworkId).tenantId(tenantId).build();
        Control c2 = Control.builder().id(control2Id).frameworkId(frameworkId).tenantId(tenantId).build();
        when(controlRepository.findByFrameworkIdAndTenantId(frameworkId, tenantId)).thenReturn(List.of(c1, c2));
        
        when(evidenceItemRepository.findByControlIdAndTenantId(control1Id, tenantId)).thenReturn(Collections.emptyList());
        when(evidenceItemRepository.findByControlIdAndTenantId(control2Id, tenantId)).thenReturn(Collections.emptyList());
        
        when(scoreRepository.save(any(AuditReadinessScore.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        AuditReadinessScore score = readinessScoreService.calculateScore(frameworkId, tenantId);

        // Assert
        assertEquals(0, score.getScore());
        assertEquals(0, score.getControlsMet());
        assertEquals(2, score.getControlsMissing());
        verify(scoreRepository).save(any(AuditReadinessScore.class));
    }

    @Test
    void scoreImprovesWithAcceptedEvidence() {
        // Arrange
        Control c1 = Control.builder().id(control1Id).frameworkId(frameworkId).tenantId(tenantId).build();
        Control c2 = Control.builder().id(control2Id).frameworkId(frameworkId).tenantId(tenantId).build();
        when(controlRepository.findByFrameworkIdAndTenantId(frameworkId, tenantId)).thenReturn(List.of(c1, c2));

        EvidenceItem e1 = EvidenceItem.builder().status(EvidenceItem.Status.ACCEPTED).build();
        when(evidenceItemRepository.findByControlIdAndTenantId(control1Id, tenantId)).thenReturn(List.of(e1));
        when(evidenceItemRepository.findByControlIdAndTenantId(control2Id, tenantId)).thenReturn(Collections.emptyList());
        
        when(scoreRepository.save(any(AuditReadinessScore.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        AuditReadinessScore score = readinessScoreService.calculateScore(frameworkId, tenantId);

        // Assert
        assertEquals(50, score.getScore());
        assertEquals(1, score.getControlsMet());
        assertEquals(1, score.getControlsMissing());
        verify(scoreRepository).save(any(AuditReadinessScore.class));
    }

    @Test
    void rejectedEvidenceDoesNotCount() {
        // Arrange
        Control c1 = Control.builder().id(control1Id).frameworkId(frameworkId).tenantId(tenantId).build();
        Control c2 = Control.builder().id(control2Id).frameworkId(frameworkId).tenantId(tenantId).build();
        when(controlRepository.findByFrameworkIdAndTenantId(frameworkId, tenantId)).thenReturn(List.of(c1, c2));

        EvidenceItem e1 = EvidenceItem.builder().status(EvidenceItem.Status.REJECTED).build();
        when(evidenceItemRepository.findByControlIdAndTenantId(control1Id, tenantId)).thenReturn(List.of(e1));
        when(evidenceItemRepository.findByControlIdAndTenantId(control2Id, tenantId)).thenReturn(Collections.emptyList());
        
        when(scoreRepository.save(any(AuditReadinessScore.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        AuditReadinessScore score = readinessScoreService.calculateScore(frameworkId, tenantId);

        // Assert
        assertEquals(0, score.getScore());
        assertEquals(0, score.getControlsMet());
        assertEquals(2, score.getControlsMissing());
        verify(scoreRepository).save(any(AuditReadinessScore.class));
    }
}
