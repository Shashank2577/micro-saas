package com.microsaas.backupvault.controller;

import com.microsaas.backupvault.service.BackupAIAnalyzerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class BackupAIController {

    private final BackupAIAnalyzerService aiAnalyzerService;

    public BackupAIController(BackupAIAnalyzerService aiAnalyzerService) {
        this.aiAnalyzerService = aiAnalyzerService;
    }

    @GetMapping("/optimize-schedule")
    public ResponseEntity<String> getOptimization(
            @RequestParam String currentSchedule,
            @RequestParam Long avgBackupSizeBytes,
            @RequestParam Long avgDurationMinutes) {
        return ResponseEntity.ok(aiAnalyzerService.suggestOptimization(currentSchedule, avgBackupSizeBytes, avgDurationMinutes));
    }
}
