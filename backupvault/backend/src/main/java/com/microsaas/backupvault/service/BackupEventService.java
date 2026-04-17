package com.microsaas.backupvault.service;

import com.microsaas.backupvault.entity.BackupExecution;
import com.microsaas.backupvault.entity.BackupRestore;
import com.microsaas.backupvault.entity.DisasterRecoveryDrill;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BackupEventService {

    public BackupEventService() {
    }

    public void publishExecutionStarted(BackupExecution execution) {
        // Mock event publish
    }

    public void publishExecutionCompleted(BackupExecution execution) {
        // Mock event publish
    }

    public void publishExecutionFailed(BackupExecution execution, String reason) {
        // Mock event publish
    }

    public void publishRestoreStarted(BackupRestore restore) {
        // Mock event publish
    }

    public void publishRestoreCompleted(BackupRestore restore) {
        // Mock event publish
    }

    public void publishDrillCompleted(DisasterRecoveryDrill drill) {
        // Mock event publish
    }
}
