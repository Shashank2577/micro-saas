package com.microsaas.backupvault.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.backupvault.dto.*;
import com.microsaas.backupvault.entity.*;
import com.microsaas.backupvault.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BackupServiceTest {

    @Mock
    private BackupPolicyRepository policyRepository;
    @Mock
    private BackupExecutionRepository executionRepository;
    @Mock
    private BackupRestoreRepository restoreRepository;
    @Mock
    private DisasterRecoveryDrillRepository drillRepository;

    @InjectMocks
    private BackupService backupService;

    private final UUID TEST_TENANT = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(TEST_TENANT);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testCreatePolicy() {
        BackupPolicyDto dto = new BackupPolicyDto();
        dto.setName("Daily Backup");
        dto.setScheduleCron("0 2 * * *");
        dto.setRetentionDays(7);
        dto.setEncryptionKeyId("key-123");
        dto.setCrossRegionReplication(true);
        dto.setTargetRegion("us-west-2");

        BackupPolicy policy = new BackupPolicy();
        policy.setId(UUID.randomUUID());
        policy.setTenantId(TEST_TENANT.toString());
        policy.setName(dto.getName());

        when(policyRepository.save(any(BackupPolicy.class))).thenReturn(policy);

        BackupPolicy result = backupService.createPolicy(dto);

        assertNotNull(result);
        assertEquals(TEST_TENANT.toString(), result.getTenantId());
        verify(policyRepository).save(any(BackupPolicy.class));
    }

    @Test
    void testExecuteBackup_UnencryptedRejected() {
        BackupExecutionDto dto = new BackupExecutionDto();
        dto.setPolicyId(UUID.randomUUID());
        dto.setBackupType("FULL");

        BackupPolicy policy = new BackupPolicy();
        policy.setId(dto.getPolicyId());
        // No encryption key set

        when(policyRepository.findById(dto.getPolicyId())).thenReturn(Optional.of(policy));

        Exception exception = assertThrows(RuntimeException.class, () -> backupService.executeBackup(dto));
        assertEquals("Unencrypted backup rejected", exception.getMessage());
    }

    @Test
    void testExecuteBackup_Success() {
        BackupExecutionDto dto = new BackupExecutionDto();
        dto.setPolicyId(UUID.randomUUID());
        dto.setBackupType("FULL");

        BackupPolicy policy = new BackupPolicy();
        policy.setId(dto.getPolicyId());
        policy.setEncryptionKeyId("key-123");
        policy.setCrossRegionReplication(true);

        BackupExecution execution = new BackupExecution();
        execution.setId(UUID.randomUUID());
        execution.setOriginalSizeBytes(100L * 1024 * 1024 * 1024);
        execution.setCompressedSizeBytes((long)(100L * 1024 * 1024 * 1024 * 0.45));

        when(policyRepository.findById(dto.getPolicyId())).thenReturn(Optional.of(policy));
        when(executionRepository.save(any(BackupExecution.class))).thenReturn(execution);

        BackupExecution result = backupService.executeBackup(dto);

        assertNotNull(result);
        assertEquals(107374182400L, result.getOriginalSizeBytes());
        assertEquals(48318382080L, result.getCompressedSizeBytes()); // 55% compression
    }

    @Test
    void testRestoreBackup_PITR() {
        BackupRestoreDto dto = new BackupRestoreDto();
        dto.setExecutionId(UUID.randomUUID());
        dto.setTargetEnvironment("test-env");
        dto.setPointInTime(LocalDateTime.now().minusHours(3));

        BackupExecution execution = new BackupExecution();
        execution.setId(dto.getExecutionId());

        BackupRestore restore = new BackupRestore();
        restore.setId(UUID.randomUUID());
        restore.setStatus("COMPLETED");

        when(executionRepository.findById(dto.getExecutionId())).thenReturn(Optional.of(execution));
        when(restoreRepository.save(any(BackupRestore.class))).thenReturn(restore);

        BackupRestore result = backupService.restoreBackup(dto);

        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
    }
    
    @Test
    void testStartDrill() {
        DisasterRecoveryDrillDto dto = new DisasterRecoveryDrillDto();
        dto.setTargetRegion("us-west-2");
        
        DisasterRecoveryDrill drill = new DisasterRecoveryDrill();
        drill.setId(UUID.randomUUID());
        drill.setRtoMinutes(45);
        drill.setSuccessful(true);
        
        when(drillRepository.save(any(DisasterRecoveryDrill.class))).thenReturn(drill);
        
        DisasterRecoveryDrill result = backupService.startDrill(dto);
        
        assertNotNull(result);
        assertTrue(result.getRtoMinutes() < 60);
        assertTrue(result.getSuccessful());
    }
}
