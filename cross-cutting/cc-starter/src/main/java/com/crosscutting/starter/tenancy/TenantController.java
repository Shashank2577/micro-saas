package com.crosscutting.starter.tenancy;

import com.crosscutting.starter.rbac.RequirePermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/cc/tenants")
@Tag(name = "Tenancy", description = "Tenant lifecycle management and onboarding")
public class TenantController {

    private final TenantService tenantService;
    private final TenantOnboardingService tenantOnboardingService;

    public TenantController(TenantService tenantService, TenantOnboardingService tenantOnboardingService) {
        this.tenantService = tenantService;
        this.tenantOnboardingService = tenantOnboardingService;
    }

    @GetMapping
    @Operation(summary = "List tenants", description = "List all tenants (paginated)")
    @ApiResponse(responseCode = "200", description = "Tenants listed successfully")
    public ResponseEntity<Page<Tenant>> listTenants(Pageable pageable) {
        return ResponseEntity.ok(tenantService.listTenants(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tenant by ID", description = "Retrieve a tenant's details by its unique identifier")
    @ApiResponse(responseCode = "200", description = "Tenant found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Tenant not found")
    public ResponseEntity<Tenant> getTenant(@Parameter(description = "Tenant ID") @PathVariable UUID id) {
        return ResponseEntity.ok(tenantService.getTenant(id));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get tenant by slug", description = "Retrieve a tenant's details by its URL-friendly slug")
    @ApiResponse(responseCode = "200", description = "Tenant found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Tenant not found")
    public ResponseEntity<Tenant> getTenantBySlug(@Parameter(description = "Tenant slug") @PathVariable String slug) {
        return ResponseEntity.ok(tenantService.getTenantBySlug(slug));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user's tenants", description = "List all tenants the authenticated user belongs to")
    @ApiResponse(responseCode = "200", description = "Tenants retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<List<Tenant>> getMyTenants() {
        UUID userId = extractUserId();
        return ResponseEntity.ok(tenantService.getTenantsForUser(userId));
    }

    // NOTE: Tenant creation is rate-limited by the global RateLimitFilter (cc.security.default-rate-limit).
    // For stricter per-user limits on tenant creation, consider adding a dedicated rate limit annotation.
    @PostMapping
    @Operation(summary = "Create a new tenant", description = "Create a new tenant and auto-assign the current user as org_admin. Subject to global rate limiting.")
    @ApiResponse(responseCode = "201", description = "Tenant created and current user onboarded as admin")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "429", description = "Rate limit exceeded")
    public ResponseEntity<Tenant> createTenant(@Valid @RequestBody CreateTenantRequest request) {
        UUID currentUserId = extractUserId();
        Tenant tenant = tenantOnboardingService.onboardTenant(request.name(), request.slug(), currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(tenant);
    }

    @PutMapping("/{id}")
    @RequirePermission(resource = "tenants", action = "write")
    @Operation(summary = "Update a tenant", description = "Update an existing tenant's name and settings")
    @ApiResponse(responseCode = "200", description = "Tenant updated successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Tenant not found")
    public ResponseEntity<Tenant> updateTenant(@Parameter(description = "Tenant ID") @PathVariable UUID id, @Valid @RequestBody UpdateTenantRequest request) {
        Tenant tenant = tenantService.updateTenant(id, request.name(), request.settings());
        return ResponseEntity.ok(tenant);
    }

    @PostMapping("/{id}/onboard")
    @RequirePermission(resource = "tenants", action = "admin")
    @Operation(summary = "Onboard a tenant", description = "Run the full onboarding flow for an existing tenant including admin user setup and default role assignment")
    @ApiResponse(responseCode = "200", description = "Tenant onboarded successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Tenant not found")
    public ResponseEntity<Tenant> onboardTenant(@Parameter(description = "Tenant ID") @PathVariable UUID id, @Valid @RequestBody OnboardRequest request) {
        Tenant tenant = tenantOnboardingService.onboardExistingTenant(id, request.adminUserId());
        return ResponseEntity.ok(tenant);
    }

    private UUID extractUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return UUID.fromString(jwt.getSubject());
        }
        throw new com.crosscutting.starter.error.CcException(
                com.crosscutting.starter.error.CcErrorCodes.UNAUTHORIZED,
                "No authenticated user found",
                401
        );
    }

    public record CreateTenantRequest(
            @NotBlank(message = "Tenant name is required") @Size(max = 255, message = "Name must not exceed 255 characters") String name,
            @NotBlank(message = "Slug is required") @Pattern(regexp = "^[a-z0-9][a-z0-9-]*[a-z0-9]$", message = "Slug must be lowercase alphanumeric with hyphens") @Size(max = 63, message = "Slug must not exceed 63 characters") String slug) {
    }

    public record UpdateTenantRequest(
            @NotBlank(message = "Tenant name is required") @Size(max = 255, message = "Name must not exceed 255 characters") String name,
            Map<String, Object> settings) {
    }

    public record OnboardRequest(
            @NotBlank(message = "Tenant name is required") @Size(max = 255, message = "Name must not exceed 255 characters") String name,
            @NotBlank(message = "Slug is required") @Pattern(regexp = "^[a-z0-9][a-z0-9-]*[a-z0-9]$", message = "Slug must be lowercase alphanumeric with hyphens") @Size(max = 63, message = "Slug must not exceed 63 characters") String slug,
            @NotNull(message = "Admin user ID is required") UUID adminUserId) {
    }
}
