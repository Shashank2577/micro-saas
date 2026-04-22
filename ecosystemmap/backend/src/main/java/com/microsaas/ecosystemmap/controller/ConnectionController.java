package com.microsaas.ecosystemmap.controller;

import com.microsaas.ecosystemmap.entity.Connection;
import com.microsaas.ecosystemmap.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ecosystems/{ecosystemId}/connections")
@RequiredArgsConstructor
public class ConnectionController {

    private final ConnectionService connectionService;

    @GetMapping
    public ResponseEntity<List<Connection>> getConnectionsByEcosystem(@PathVariable UUID ecosystemId) {
        return ResponseEntity.ok(connectionService.getConnectionsByEcosystem(ecosystemId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Connection> getConnectionById(@PathVariable UUID ecosystemId, @PathVariable UUID id) {
        return ResponseEntity.ok(connectionService.getConnectionById(id));
    }

    @PostMapping
    public ResponseEntity<Connection> createConnection(
            @PathVariable UUID ecosystemId,
            @RequestParam UUID sourceNodeId,
            @RequestParam UUID targetNodeId,
            @RequestBody Connection connection) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(connectionService.createConnection(ecosystemId, sourceNodeId, targetNodeId, connection));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Connection> updateConnection(
            @PathVariable UUID ecosystemId,
            @PathVariable UUID id,
            @RequestBody Connection connection) {
        return ResponseEntity.ok(connectionService.updateConnection(id, connection));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConnection(@PathVariable UUID ecosystemId, @PathVariable UUID id) {
        connectionService.deleteConnection(id);
        return ResponseEntity.noContent().build();
    }
}
