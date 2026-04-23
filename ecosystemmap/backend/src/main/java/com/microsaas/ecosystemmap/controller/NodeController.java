package com.microsaas.ecosystemmap.controller;

import com.microsaas.ecosystemmap.entity.Node;
import com.microsaas.ecosystemmap.service.NodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ecosystems/{ecosystemId}/nodes")
@RequiredArgsConstructor
public class NodeController {

    private final NodeService nodeService;

    @GetMapping
    public ResponseEntity<List<Node>> getNodesByEcosystem(@PathVariable UUID ecosystemId) {
        return ResponseEntity.ok(nodeService.getNodesByEcosystem(ecosystemId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Node> getNodeById(@PathVariable UUID ecosystemId, @PathVariable UUID id) {
        return ResponseEntity.ok(nodeService.getNodeById(id));
    }

    @PostMapping
    public ResponseEntity<Node> createNode(@PathVariable UUID ecosystemId, @RequestBody Node node) {
        return ResponseEntity.status(HttpStatus.CREATED).body(nodeService.createNode(ecosystemId, node));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Node> updateNode(@PathVariable UUID ecosystemId, @PathVariable UUID id, @RequestBody Node node) {
        return ResponseEntity.ok(nodeService.updateNode(id, node));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNode(@PathVariable UUID ecosystemId, @PathVariable UUID id) {
        nodeService.deleteNode(id);
        return ResponseEntity.noContent().build();
    }
}
