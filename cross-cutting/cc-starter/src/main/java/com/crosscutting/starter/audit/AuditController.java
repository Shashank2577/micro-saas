package com.crosscutting.starter.audit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/cc/audit")
@Tag(name = "Audit", description = "Query system, business, and auth audit logs")
public class AuditController {

    private final SystemAuditService systemAuditService;
    private final BusinessAuditService businessAuditService;
    private final AuthEventRepository authEventRepository;

    public AuditController(SystemAuditService systemAuditService,
                           BusinessAuditService businessAuditService,
                           AuthEventRepository authEventRepository) {
        this.systemAuditService = systemAuditService;
        this.businessAuditService = businessAuditService;
        this.authEventRepository = authEventRepository;
    }

    @GetMapping("/system")
    @Operation(summary = "Query system audit logs", description = "Retrieve paginated system audit logs with optional filters by tenant, user, or event type")
    @ApiResponse(responseCode = "200", description = "System audit logs retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public Page<SystemAuditLog> getSystemAuditLogs(
            @Parameter(description = "Filter by tenant ID") @RequestParam(required = false) UUID tenantId,
            @Parameter(description = "Filter by user ID") @RequestParam(required = false) UUID userId,
            @Parameter(description = "Filter by event type") @RequestParam(required = false) String eventType,
            Pageable pageable) {

        if (tenantId != null && eventType != null) {
            return systemAuditService.findByTenantAndEventType(tenantId, eventType, pageable);
        }
        if (tenantId != null) {
            return systemAuditService.findByTenant(tenantId, pageable);
        }
        if (userId != null) {
            return systemAuditService.findByUser(userId, pageable);
        }
        if (eventType != null) {
            return systemAuditService.findByEventType(eventType, pageable);
        }

        return systemAuditService.findByTenant(null, pageable);
    }

    @GetMapping("/business")
    @Operation(summary = "Query business audit logs", description = "Retrieve paginated business audit logs with optional filters by tenant or user")
    @ApiResponse(responseCode = "200", description = "Business audit logs retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public Page<BusinessAuditLog> getBusinessAuditLogs(
            @Parameter(description = "Filter by tenant ID") @RequestParam(required = false) UUID tenantId,
            @Parameter(description = "Filter by user ID") @RequestParam(required = false) UUID userId,
            Pageable pageable) {

        if (tenantId != null) {
            return businessAuditService.findByTenant(tenantId, pageable);
        }
        if (userId != null) {
            return businessAuditService.findByUser(userId, pageable);
        }

        return businessAuditService.findByTenant(null, pageable);
    }

    @GetMapping("/auth")
    @Operation(summary = "Query auth events", description = "Retrieve paginated authentication events with optional filters by user, event type, or tenant")
    @ApiResponse(responseCode = "200", description = "Auth events retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public Page<AuthEvent> getAuthEvents(
            @Parameter(description = "Filter by user ID") @RequestParam(required = false) UUID userId,
            @Parameter(description = "Filter by event type") @RequestParam(required = false) String eventType,
            @Parameter(description = "Filter by tenant ID") @RequestParam(required = false) UUID tenantId,
            Pageable pageable) {

        if (userId != null) {
            return authEventRepository.findByUserId(userId, pageable);
        }
        if (eventType != null) {
            return authEventRepository.findByEventType(eventType, pageable);
        }
        if (tenantId != null) {
            return authEventRepository.findByTenantId(tenantId, pageable);
        }

        return authEventRepository.findAll(pageable);
    }

    @GetMapping("/resource/{type}/{id}")
    @Operation(summary = "Get audit trail for a resource", description = "Retrieve combined system and business audit logs for a specific resource")
    @ApiResponse(responseCode = "200", description = "Resource audit trail retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Resource not found")
    public Map<String, Object> getResourceAudit(
            @Parameter(description = "Resource type") @PathVariable String type,
            @Parameter(description = "Resource ID") @PathVariable String id,
            Pageable pageable) {

        Map<String, Object> result = new HashMap<>();

        // System audit logs for this resource
        result.put("systemLogs", systemAuditService.findByResource(type, id, pageable));

        // Business audit logs for this resource (try parsing id as UUID)
        try {
            UUID resourceId = UUID.fromString(id);
            result.put("businessLogs", businessAuditService.findByResource(type, resourceId, pageable));
        } catch (IllegalArgumentException e) {
            // Resource ID is not a UUID, skip business logs
            result.put("businessLogs", Page.empty());
        }

        return result;
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get full audit trail for a user", description = "Retrieve combined system, business, and auth audit logs for a specific user")
    @ApiResponse(responseCode = "200", description = "User audit trail retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "User not found")
    public Map<String, Object> getUserAudit(
            @Parameter(description = "User ID") @PathVariable UUID userId,
            Pageable pageable) {

        Map<String, Object> result = new HashMap<>();
        result.put("systemLogs", systemAuditService.findByUser(userId, pageable));
        result.put("businessLogs", businessAuditService.findByUser(userId, pageable));
        result.put("authEvents", authEventRepository.findByUserId(userId, pageable));

        return result;
    }
}
