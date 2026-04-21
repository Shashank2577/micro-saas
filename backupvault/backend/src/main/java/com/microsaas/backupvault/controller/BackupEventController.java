package com.microsaas.backupvault.controller;

import com.microsaas.backupvault.entity.BackupExecution;
import com.microsaas.backupvault.entity.BackupRestore;
import com.microsaas.backupvault.entity.DisasterRecoveryDrill;
import com.microsaas.backupvault.service.BackupEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class BackupEventController {

    private final BackupEventService eventService;

    @PostMapping("/execution-started")
    public ResponseEntity<?> publishExecutionStarted(@RequestBody BackupExecution execution) {
        eventService.publishExecutionStarted(execution);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/execution-completed")
    public ResponseEntity<?> publishExecutionCompleted(@RequestBody BackupExecution execution) {
        eventService.publishExecutionCompleted(execution);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/execution-failed")
    public ResponseEntity<?> publishExecutionFailed(@RequestBody BackupExecution execution, @RequestParam String reason) {
        eventService.publishExecutionFailed(execution, reason);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/restore-started")
    public ResponseEntity<?> publishRestoreStarted(@RequestBody BackupRestore restore) {
        eventService.publishRestoreStarted(restore);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/restore-completed")
    public ResponseEntity<?> publishRestoreCompleted(@RequestBody BackupRestore restore) {
        eventService.publishRestoreCompleted(restore);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/drill-completed")
    public ResponseEntity<?> publishDrillCompleted(@RequestBody DisasterRecoveryDrill drill) {
        eventService.publishDrillCompleted(drill);
        return ResponseEntity.ok().build();
    }
}
