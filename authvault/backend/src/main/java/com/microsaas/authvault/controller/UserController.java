package com.microsaas.authvault.controller;

import com.microsaas.authvault.dto.UserDto;
import com.microsaas.authvault.entity.User;
import com.microsaas.authvault.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserDto dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping("/{id}/mfa/setup")
    public ResponseEntity<String> setupMfa(@PathVariable UUID id) {
        return ResponseEntity.ok(authService.setupMfa(id));
    }
}
