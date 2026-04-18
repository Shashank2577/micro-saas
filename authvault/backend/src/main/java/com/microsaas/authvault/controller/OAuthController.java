package com.microsaas.authvault.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/oauth2")
public class OAuthController {

    @GetMapping("/authorize")
    public ResponseEntity<String> authorize() {
        return ResponseEntity.ok("Authorization flow simulated");
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> token() {
        return ResponseEntity.ok(Map.of("access_token", "dummy-token", "token_type", "Bearer", "expires_in", "3600"));
    }
}
