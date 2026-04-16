package com.crosscutting.starter.rbac;

import com.crosscutting.starter.error.CcErrorCodes;
import com.crosscutting.starter.tenancy.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/cc/rbac")
@Tag(name = "RBAC", description = "Role-based access control - roles, permissions, and assignments")
public class RbacController {

    private final RbacService rbacService;
    private final RoleRepository roleRepository;

    public RbacController(RbacService rbacService, RoleRepository roleRepository) {
        this.rbacService = rbacService;
        this.roleRepository = roleRepository;
    }

    /**
     * Get current user's permissions in the current tenant.
     */
    @GetMapping("/permissions")
    @Operation(summary = "Get current user's permissions", description = "Returns the set of permission keys for the authenticated user within the current tenant context")
    @ApiResponse(responseCode = "200", description = "Permissions retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public Set<String> getPermissions() {
        UUID userId = currentUserId();
        UUID tenantId = TenantContext.require();
        return rbacService.getPermissions(userId, tenantId);
    }

    /**
     * List roles available for the current tenant (system + tenant-specific).
     */
    @GetMapping("/roles")
    @Operation(summary = "List available roles", description = "Returns system-wide roles and tenant-specific custom roles available in the current tenant")
    @ApiResponse(responseCode = "200", description = "Roles listed successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public List<Role> listRoles() {
        UUID tenantId = TenantContext.require();
        return roleRepository.findByTenantIdOrTenantIdIsNull(tenantId);
    }

    /**
     * Create a custom role scoped to the current tenant.
     */
    @PostMapping("/roles")
    @RequirePermission(resource = "roles", action = "create")
    @Operation(summary = "Create a custom role", description = "Create a new role scoped to the current tenant with the specified permissions")
    @ApiResponse(responseCode = "200", description = "Role created successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden - requires roles:create permission")
    public Role createRole(@Valid @RequestBody CreateRoleRequest request) {
        UUID tenantId = TenantContext.require();
        return rbacService.createRole(tenantId, request.name(), request.description(), request.permissionIds());
    }

    /**
     * Assign a role to a user in the current tenant.
     */
    @PostMapping("/roles/{roleId}/assign/{userId}")
    @RequirePermission(resource = "roles", action = "assign")
    @Operation(summary = "Assign a role to a user", description = "Grant a role to a user within the current tenant context")
    @ApiResponse(responseCode = "200", description = "Role assigned successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden - requires roles:assign permission")
    @ApiResponse(responseCode = "404", description = "Role or user not found")
    public Map<String, String> assignRole(
            @Parameter(description = "Role ID to assign") @PathVariable UUID roleId,
            @Parameter(description = "Target user ID") @PathVariable UUID userId) {
        UUID tenantId = TenantContext.require();
        rbacService.assignRole(userId, roleId, tenantId);
        return Map.of("status", "assigned");
    }

    /**
     * Remove a role from a user in the current tenant.
     */
    @DeleteMapping("/roles/{roleId}/revoke/{userId}")
    @RequirePermission(resource = "roles", action = "revoke")
    @Operation(summary = "Revoke a role from a user", description = "Remove a role assignment from a user within the current tenant context")
    @ApiResponse(responseCode = "200", description = "Role revoked successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden - requires roles:revoke permission")
    @ApiResponse(responseCode = "404", description = "Role assignment not found")
    public Map<String, String> revokeRole(
            @Parameter(description = "Role ID to revoke") @PathVariable UUID roleId,
            @Parameter(description = "Target user ID") @PathVariable UUID userId) {
        UUID tenantId = TenantContext.require();
        rbacService.removeRole(userId, roleId, tenantId);
        return Map.of("status", "revoked");
    }

    /**
     * Check if the current user has a specific permission.
     */
    @GetMapping("/check")
    @Operation(summary = "Check a permission", description = "Verify whether the authenticated user has a specific resource:action permission in the current tenant")
    @ApiResponse(responseCode = "200", description = "Permission check result returned")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public Map<String, Boolean> checkPermission(
            @Parameter(description = "Resource name to check") @RequestParam String resource,
            @Parameter(description = "Action to check") @RequestParam String action) {
        UUID userId = currentUserId();
        UUID tenantId = TenantContext.require();
        boolean allowed = rbacService.hasPermission(userId, tenantId, resource, action);
        return Map.of("allowed", allowed);
    }

    private UUID currentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            return UUID.fromString(jwt.getSubject());
        }
        throw CcErrorCodes.unauthorized("Cannot determine user identity");
    }

    public record CreateRoleRequest(
            @NotBlank(message = "Role name is required") @Size(max = 100, message = "Role name must not exceed 100 characters") String name,
            @Size(max = 500, message = "Description must not exceed 500 characters") String description,
            @NotEmpty(message = "At least one permission is required") List<UUID> permissionIds) {
    }
}
