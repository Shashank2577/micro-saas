package com.microsaas.identitycore.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.identitycore.model.User;
import com.microsaas.identitycore.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private UUID getTenantId() {
        return UUID.fromString(TenantContext.require().toString());
    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(userService.getUsersByTenant(getTenantId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable UUID id) {
        return userService.getUserById(id, getTenantId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(getTenantId(), user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, getTenantId(), user));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
