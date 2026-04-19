package com.microsaas.deploysignal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/routing")
public class ApiKeyRoutingController {

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> testRouting(@RequestHeader(value = "X-API-Key", required = false) String apiKey) {
        Map<String, String> response = new HashMap<>();

        if (apiKey == null || apiKey.isEmpty()) {
            response.put("status", "error");
            response.put("message", "Missing API Key");
            return ResponseEntity.status(401).body(response);
        }

        if (apiKey.startsWith("wave2-")) {
            response.put("status", "success");
            response.put("route", "wave2-cluster");
            response.put("message", "Routed to Wave2 infrastructure");
            return ResponseEntity.ok(response);
        } else if (apiKey.startsWith("wave1-")) {
            response.put("status", "success");
            response.put("route", "wave1-cluster");
            response.put("message", "Routed to Wave1 infrastructure");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Invalid API Key prefix for routing");
            return ResponseEntity.status(403).body(response);
        }
    }
}
