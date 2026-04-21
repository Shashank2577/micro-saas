package com.microsaas.integrationmesh.controller;

import com.microsaas.integrationmesh.model.SyncHistory;
import com.microsaas.integrationmesh.service.SyncHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sync-history")
@RequiredArgsConstructor
public class SyncHistoryController {

    private final SyncHistoryService syncHistoryService;

    @GetMapping("/{integrationId}")
    public ResponseEntity<List<SyncHistory>> getHistory(@PathVariable UUID integrationId) {
        return ResponseEntity.ok(syncHistoryService.getHistory(integrationId));
    }
}
