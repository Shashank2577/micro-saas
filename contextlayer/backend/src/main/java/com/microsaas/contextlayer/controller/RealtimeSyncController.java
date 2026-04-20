package com.microsaas.contextlayer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RealtimeSyncController {

    @GetMapping("/api/context-sync/watch")
    public ResponseEntity<String> watchContext(@RequestParam String customerId) {
        // Simplified HTTP implementation in lieu of full WebSocket setup
        return ResponseEntity.ok("Watching customer: " + customerId);
    }
}
