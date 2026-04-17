package com.crosscutting.starter.notifications;

import com.crosscutting.starter.tenancy.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/cc/notifications")
@Tag(name = "Notifications", description = "User notifications - send, list, read status, and preferences")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * List notifications for a user (paginated).
     */
    @GetMapping
    @Operation(summary = "List notifications", description = "Retrieve paginated notifications for a user within the current tenant")
    @ApiResponse(responseCode = "200", description = "Notifications listed successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public Page<Notification> list(@Parameter(description = "User ID") @RequestParam UUID userId, Pageable pageable) {
        UUID tenantId = TenantContext.require();
        return notificationService.listForUser(userId, tenantId, pageable);
    }

    /**
     * Mark a notification as read.
     */
    @PutMapping("/{id}/read")
    @Operation(summary = "Mark notification as read", description = "Update a notification's status to read")
    @ApiResponse(responseCode = "200", description = "Notification marked as read")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Notification not found")
    public Notification markAsRead(@Parameter(description = "Notification ID") @PathVariable UUID id) {
        return notificationService.markAsRead(id);
    }

    /**
     * Send a notification.
     */
    @PostMapping("/send")
    @Operation(summary = "Send a notification", description = "Send a notification to a user via the specified channel")
    @ApiResponse(responseCode = "200", description = "Notification sent successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public Notification send(@Valid @RequestBody SendNotificationRequest request) {
        UUID tenantId = TenantContext.require();
        return notificationService.send(
                request.userId(), tenantId, request.channel(),
                request.title(), request.body(), request.data() != null ? request.data() : Map.of());
    }

    /**
     * Get notification preferences for a user.
     */
    @GetMapping("/preferences")
    @Operation(summary = "Get notification preferences", description = "Retrieve notification channel preferences for a user in the current tenant")
    @ApiResponse(responseCode = "200", description = "Preferences retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public List<NotificationPreference> getPreferences(@Parameter(description = "User ID") @RequestParam UUID userId) {
        return notificationService.getPreferences(userId);
    }

    /**
     * Update a notification preference.
     */
    @PutMapping("/preferences")
    @Operation(summary = "Update notification preference", description = "Enable or disable a notification channel for a user in the current tenant")
    @ApiResponse(responseCode = "200", description = "Preference updated successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public NotificationPreference updatePreference(@Valid @RequestBody UpdatePreferenceRequest request) {
        return notificationService.updatePreference(
                request.userId(), request.channel(), request.category(), request.enabled());
    }

    public record SendNotificationRequest(
            @NotNull(message = "User ID is required") UUID userId,
            @NotBlank(message = "Channel is required") @Size(max = 50, message = "Channel must not exceed 50 characters") String channel,
            @NotBlank(message = "Title is required") @Size(max = 255, message = "Title must not exceed 255 characters") String title,
            @NotBlank(message = "Body is required") @Size(max = 5000, message = "Body must not exceed 5000 characters") String body,
            Map<String, Object> data) {
    }

    public record UpdatePreferenceRequest(
            @NotNull(message = "User ID is required") UUID userId,
            @NotBlank(message = "Channel is required") @Size(max = 50, message = "Channel must not exceed 50 characters") String channel,
            @NotBlank(message = "Category is required") @Size(max = 100, message = "Category must not exceed 100 characters") String category,
            boolean enabled) {
    }
}
