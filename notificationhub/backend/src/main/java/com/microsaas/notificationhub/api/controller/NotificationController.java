package com.microsaas.notificationhub.api.controller;

import com.microsaas.notificationhub.api.dto.NotificationDto;
import com.microsaas.notificationhub.api.dto.SendBatchNotificationRequest;
import com.microsaas.notificationhub.api.dto.SendNotificationRequest;
import com.microsaas.notificationhub.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationDto sendNotification(@RequestHeader("X-Tenant-ID") String tenantId,
                                            @Valid @RequestBody SendNotificationRequest request) {
        return notificationService.sendNotification(tenantId, request);
    }

    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    public List<NotificationDto> sendBatchNotification(@RequestHeader("X-Tenant-ID") String tenantId,
                                                       @Valid @RequestBody SendBatchNotificationRequest request) {
        return notificationService.sendBatchNotification(tenantId, request);
    }

    @GetMapping("/{notificationId}")
    public NotificationDto getNotification(@RequestHeader("X-Tenant-ID") String tenantId,
                                           @PathVariable UUID notificationId) {
        return notificationService.getNotification(tenantId, notificationId);
    }

    @GetMapping("/history")
    public List<NotificationDto> getHistory(@RequestHeader("X-Tenant-ID") String tenantId) {
        return notificationService.getHistory(tenantId);
    }
}
