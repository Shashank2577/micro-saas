package com.crosscutting.starter.flags;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/cc/flags")
@Tag(name = "Feature Flags", description = "Feature flag evaluation, creation, and override management")
public class FeatureFlagController {

    private final FeatureFlagService featureFlagService;

    public FeatureFlagController(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    /**
     * Evaluate all flags for a given tenant and user context.
     */
    @GetMapping
    @Operation(summary = "Evaluate all flags", description = "Evaluate all feature flags for a given tenant and user context, returning a map of flag keys to enabled state")
    @ApiResponse(responseCode = "200", description = "Flags evaluated successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public Map<String, Boolean> evaluateAll(
            @Parameter(description = "Tenant ID context") @RequestParam(required = false) UUID tenantId,
            @Parameter(description = "User ID context") @RequestParam(required = false) UUID userId) {
        return featureFlagService.evaluateAll(tenantId, userId);
    }

    /**
     * Evaluate a single flag by key.
     */
    @GetMapping("/{key}")
    @Operation(summary = "Evaluate a single flag", description = "Check whether a specific feature flag is enabled for the given tenant and user context")
    @ApiResponse(responseCode = "200", description = "Flag evaluated successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Flag not found")
    public Map<String, Boolean> evaluateFlag(
            @Parameter(description = "Feature flag key") @PathVariable String key,
            @Parameter(description = "Tenant ID context") @RequestParam(required = false) UUID tenantId,
            @Parameter(description = "User ID context") @RequestParam(required = false) UUID userId) {
        boolean enabled = featureFlagService.isEnabled(key, tenantId, userId);
        return Map.of("enabled", enabled);
    }

    /**
     * Create a new feature flag.
     */
    @PostMapping
    @Operation(summary = "Create a feature flag", description = "Define a new feature flag with a key, description, and default enabled state")
    @ApiResponse(responseCode = "200", description = "Flag created successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public FeatureFlag createFlag(@Valid @RequestBody CreateFlagRequest request) {
        return featureFlagService.createFlag(request.key(), request.description(), request.defaultEnabled());
    }

    /**
     * Set an override for a feature flag.
     */
    @PutMapping("/{key}/override")
    @Operation(summary = "Set a flag override", description = "Create or update a tenant/user-specific override for a feature flag")
    @ApiResponse(responseCode = "200", description = "Override set successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Flag not found")
    public FlagOverride setOverride(@Parameter(description = "Feature flag key") @PathVariable String key, @RequestBody SetOverrideRequest request) {
        return featureFlagService.setOverride(key, request.tenantId(), request.userId(), request.enabled());
    }

    public record CreateFlagRequest(
            @NotBlank(message = "Flag key is required") @Size(max = 100, message = "Key must not exceed 100 characters") String key,
            @Size(max = 500, message = "Description must not exceed 500 characters") String description,
            boolean defaultEnabled) {
    }

    public record SetOverrideRequest(UUID tenantId, UUID userId, boolean enabled) {
    }
}
