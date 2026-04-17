package com.microsaas.backupvault.controller;

import com.microsaas.backupvault.dto.*;
import com.microsaas.backupvault.entity.*;
import com.microsaas.backupvault.service.BackupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class BackupController {

    private final BackupService backupService;

    public BackupController(BackupService backupService) {
        this.backupService = backupService;
    }

    @PostMapping("/policies")
    public ResponseEntity<BackupPolicy> createPolicy(@RequestBody BackupPolicyDto dto) {
        return ResponseEntity.ok(backupService.createPolicy(dto));
    }

    @GetMapping("/policies")
    public ResponseEntity<List<BackupPolicy>> getPolicies() {
        return ResponseEntity.ok(backupService.getPolicies());
    }

    @GetMapping("/policies/{id}")
    public ResponseEntity<BackupPolicy> getPolicy(@PathVariable UUID id) {
        return ResponseEntity.ok(backupService.getPolicy(id));
    }

    @PostMapping("/executions")
    public ResponseEntity<BackupExecution> executeBackup(@RequestBody BackupExecutionDto dto) {
        return ResponseEntity.ok(backupService.executeBackup(dto));
    }

    @GetMapping("/executions")
    public ResponseEntity<List<BackupExecution>> getExecutions() {
        return ResponseEntity.ok(backupService.getExecutions());
    }

    @GetMapping("/executions/{id}")
    public ResponseEntity<BackupExecution> getExecution(@PathVariable UUID id) {
        return ResponseEntity.ok(backupService.getExecution(id));
    }

    @PostMapping("/restores")
    public ResponseEntity<BackupRestore> restoreBackup(@RequestBody BackupRestoreDto dto) {
        return ResponseEntity.ok(backupService.restoreBackup(dto));
    }

    @GetMapping("/restores")
    public ResponseEntity<List<BackupRestore>> getRestores() {
        return ResponseEntity.ok(backupService.getRestores());
    }

    @PostMapping("/dr-drills")
    public ResponseEntity<DisasterRecoveryDrill> startDrill(@RequestBody DisasterRecoveryDrillDto dto) {
        return ResponseEntity.ok(backupService.startDrill(dto));
    }
}
