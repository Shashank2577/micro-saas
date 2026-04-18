package com.microsaas.authvault.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/social")
public class SocialLoginController {

    @GetMapping("/{provider}/callback")
    public ResponseEntity<String> callback(@PathVariable String provider, @RequestParam String code) {
        return ResponseEntity.ok("Social login callback success for " + provider);
    }
}
