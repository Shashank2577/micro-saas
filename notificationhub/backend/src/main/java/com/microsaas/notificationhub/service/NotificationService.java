package com.microsaas.notificationhub.service;

import com.microsaas.notificationhub.api.dto.NotificationDto;
import com.microsaas.notificationhub.api.dto.SendBatchNotificationRequest;
import com.microsaas.notificationhub.api.dto.SendNotificationRequest;
import com.microsaas.notificationhub.domain.entity.Notification;
import com.microsaas.notificationhub.domain.entity.NotificationTemplate;
import com.microsaas.notificationhub.domain.repository.NotificationRepository;
import com.microsaas.notificationhub.domain.repository.NotificationTemplateRepository;
import com.microsaas.notificationhub.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository templateRepository;
    private final PreferenceService preferenceService;
    private final DeliveryService deliveryService;
    private final ContentOptimizationService optimizationService;

    @Transactional
    public NotificationDto sendNotification(String tenantId, SendNotificationRequest request) {
        return processNotification(tenantId, request.getUserId(), request.getTemplateId(),
                request.getChannel(), request.getVariables(), request.getScheduledFor());
    }

    @Transactional
    public List<NotificationDto> sendBatchNotification(String tenantId, SendBatchNotificationRequest request) {
        List<NotificationDto> results = new ArrayList<>();
        for (String userId : request.getUserIds()) {
            try {
                NotificationDto dto = processNotification(tenantId, userId, request.getTemplateId(),
                        request.getChannel(), request.getVariables(), request.getScheduledFor());
                results.add(dto);
            } catch (Exception e) {
                log.error("Failed to process notification for user {}", userId, e);
            }
        }
        return results;
    }

    private NotificationDto processNotification(String tenantId, String userId, UUID templateId,
                                                String channel, Map<String, String> variables, ZonedDateTime scheduledFor) {

        if (!preferenceService.isUserOptedIn(tenantId, userId, channel)) {
            log.info("User {} is opted out of channel {}", userId, channel);
            throw new IllegalArgumentException("User opted out of this channel");
        }

        // Check rate limits before proceeding
        if (!preferenceService.checkRateLimit(tenantId, userId, channel)) {
            log.warn("Rate limit exceeded for user {} on channel {}", userId, channel);
            throw new IllegalArgumentException("Rate limit exceeded for user on this channel");
        }

        NotificationTemplate template = templateRepository.findByIdAndTenantId(templateId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        String subject = substituteVariables(template.getSubjectTemplate(), variables);
        String content = substituteVariables(template.getContentTemplate(), variables);

        // A/B Testing / Content Optimization based on goal if passed in variables
        if (variables != null && variables.containsKey("optimization_goal")) {
            content = optimizationService.optimizeContent(content, variables.get("optimization_goal"));
        }

        String status = (scheduledFor != null && scheduledFor.isAfter(ZonedDateTime.now())) ? "SCHEDULED" : "PENDING";

        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .tenantId(tenantId)
                .userId(userId)
                .template(template)
                .channel(channel)
                .subject(subject)
                .content(content)
                .status(status)
                .scheduledFor(scheduledFor)
                .build();

        notificationRepository.save(notification);

        if ("PENDING".equals(status)) {
            deliveryService.deliverNotification(notification);
        }

        return mapToDto(notification);
    }

    private String substituteVariables(String text, Map<String, String> variables) {
        if (text == null) return null;
        if (variables == null) return text;

        String result = text;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<NotificationDto> getScheduledNotifications(String tenantId) {
        return notificationRepository.findByTenantId(tenantId).stream()
                .filter(n -> "SCHEDULED".equals(n.getStatus()))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public NotificationDto updateScheduledNotification(String tenantId, UUID id, SendNotificationRequest request) {
        Notification notification = notificationRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        if (!"SCHEDULED".equals(notification.getStatus())) {
            throw new IllegalStateException("Only scheduled notifications can be updated");
        }

        NotificationTemplate template = templateRepository.findByIdAndTenantId(request.getTemplateId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        notification.setTemplate(template);
        notification.setSubject(substituteVariables(template.getSubjectTemplate(), request.getVariables()));
        notification.setContent(substituteVariables(template.getContentTemplate(), request.getVariables()));
        notification.setScheduledFor(request.getScheduledFor());

        notificationRepository.save(notification);
        return mapToDto(notification);
    }

    @Transactional
    public void cancelScheduledNotification(String tenantId, UUID id) {
        Notification notification = notificationRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        if (!"SCHEDULED".equals(notification.getStatus())) {
            throw new IllegalStateException("Only scheduled notifications can be cancelled");
        }

        notificationRepository.delete(notification);
    }

    @Transactional(readOnly = true)
    public NotificationDto getNotification(String tenantId, UUID id) {
        return notificationRepository.findByIdAndTenantId(id, tenantId)
                .map(this::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
    }

    @Transactional(readOnly = true)
    public List<NotificationDto> getHistory(String tenantId) {
        return notificationRepository.findByTenantId(tenantId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private NotificationDto mapToDto(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .templateId(notification.getTemplate() != null ? notification.getTemplate().getId() : null)
                .channel(notification.getChannel())
                .subject(notification.getSubject())
                .content(notification.getContent())
                .status(notification.getStatus())
                .scheduledFor(notification.getScheduledFor())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
