package com.microsaas.notificationhub.api.controller;

import com.microsaas.notificationhub.api.dto.NotificationDto;
import com.microsaas.notificationhub.api.dto.SendNotificationRequest;
import com.microsaas.notificationhub.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/scheduled-notifications")
@RequiredArgsConstructor
public class ScheduledNotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationDto scheduleNotification(@RequestHeader("X-Tenant-ID") String tenantId,
                                                @Valid @RequestBody SendNotificationRequest request) {
        if (request.getScheduledFor() == null) {
            throw new IllegalArgumentException("scheduledFor must be provided");
        }
        return notificationService.sendNotification(tenantId, request);
    }

    @GetMapping
    public List<NotificationDto> getScheduledNotifications(@RequestHeader("X-Tenant-ID") String tenantId) {
        return notificationService.getScheduledNotifications(tenantId);
    }

    @PutMapping("/{notificationId}")
    public NotificationDto updateScheduledNotification(@RequestHeader("X-Tenant-ID") String tenantId,
                                                       @PathVariable UUID notificationId,
                                                       @Valid @RequestBody SendNotificationRequest request) {
        return notificationService.updateScheduledNotification(tenantId, notificationId, request);
    }

    @DeleteMapping("/{notificationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteScheduledNotification(@RequestHeader("X-Tenant-ID") String tenantId,
                                            @PathVariable UUID notificationId) {
        notificationService.cancelScheduledNotification(tenantId, notificationId);
    }
}
