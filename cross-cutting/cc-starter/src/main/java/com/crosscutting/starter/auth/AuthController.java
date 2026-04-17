package com.crosscutting.starter.auth;

import com.crosscutting.starter.error.CcErrorCodes;
import com.crosscutting.starter.rbac.RequirePermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/cc/auth")
@Tag(name = "Auth", description = "Authentication and user identity operations")
public class AuthController {

    private final UserSyncService userSyncService;

    public AuthController(UserSyncService userSyncService) {
        this.userSyncService = userSyncService;
    }

    @GetMapping("/me")
    @Operation(summary = "Get current principal", description = "Returns the authenticated user's identity and claims from the JWT token")
    @ApiResponse(responseCode = "200", description = "Current principal retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized - no valid token provided")
    public CcPrincipal me() {
        return CcPrincipal.current();
    }

    @GetMapping("/user/{id}")
    @RequirePermission(resource = "users", action = "read")
    @Operation(summary = "Get user by ID", description = "Look up a synced user record by their unique identifier")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    @ApiResponse(responseCode = "404", description = "User not found")
    public User getUser(@Parameter(description = "User ID") @PathVariable UUID id) {
        return userSyncService.findById(id)
                .orElseThrow(() -> CcErrorCodes.resourceNotFound("User not found: " + id));
    }
}
