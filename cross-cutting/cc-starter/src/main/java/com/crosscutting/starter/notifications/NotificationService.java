package com.crosscutting.starter.notifications;

import com.crosscutting.starter.error.CcErrorCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final NotificationPreferenceRepository preferenceRepository;
    private final Map<String, NotificationChannel> channels;

    public NotificationService(NotificationRepository notificationRepository,
                               NotificationPreferenceRepository preferenceRepository,
                               List<NotificationChannel> channelList) {
        this.notificationRepository = notificationRepository;
        this.preferenceRepository = preferenceRepository;
        this.channels = channelList.stream()
                .collect(Collectors.toMap(NotificationChannel::channelName, Function.identity()));
    }

    /**
     * Send a notification via the specified channel.
     * Checks user preferences before dispatching.
     */
    @Transactional
    public Notification send(UUID userId, UUID tenantId, String channel, String title, String body, Map<String, Object> data) {
        return send(userId, tenantId, channel, title, body, data, "general");
    }

    /**
     * Send a notification via the specified channel and category.
     * Checks user preferences for the specific channel+category combination before dispatching.
     */
    @Transactional
    public Notification send(UUID userId, UUID tenantId, String channel, String title, String body, Map<String, Object> data, String category) {
        // Check if channel implementation exists
        NotificationChannel channelImpl = channels.get(channel);
        if (channelImpl == null) {
            throw CcErrorCodes.validationError("Unknown notification channel: " + channel);
        }

        // Check user preference for this specific channel+category combination
        preferenceRepository.findByUserIdAndChannelAndCategory(userId, channel, category)
                .ifPresent(pref -> {
                    if (!pref.isEnabled()) {
                        throw CcErrorCodes.validationError(
                                "User has disabled notifications for channel: " + channel + ", category: " + category);
                    }
                });

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTenantId(tenantId);
        notification.setChannel(channel);
        notification.setTitle(title);
        notification.setBody(body);
        notification.setData(data != null ? data : Map.of());

        notification = notificationRepository.save(notification);

        channelImpl.send(notification);

        notification.setSentAt(Instant.now());
        notification = notificationRepository.save(notification);

        log.info("Notification sent via {} to user {} in tenant {}", channel, userId, tenantId);
        return notification;
    }

    /**
     * Mark a notification as read.
     */
    @Transactional
    public Notification markAsRead(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> CcErrorCodes.resourceNotFound("Notification not found: " + id));
        notification.setReadAt(Instant.now());
        return notificationRepository.save(notification);
    }

    /**
     * List notifications for a user in a tenant (paginated).
     */
    public Page<Notification> listForUser(UUID userId, UUID tenantId, Pageable pageable) {
        return notificationRepository.findByUserIdAndTenantId(userId, tenantId, pageable);
    }

    /**
     * Get notification preferences for a user in a tenant.
     */
    public List<NotificationPreference> getPreferences(UUID userId) {
        return preferenceRepository.findByUserId(userId);
    }

    /**
     * Update a notification preference for a user/tenant/channel.
     * Creates the preference if it does not exist.
     */
    @Transactional
    public NotificationPreference updatePreference(UUID userId, String channel, String category, boolean enabled) {
        NotificationPreference preference = preferenceRepository
                .findByUserIdAndChannelAndCategory(userId, channel, category)
                .orElseGet(() -> {
                    NotificationPreference p = new NotificationPreference();
                    p.setUserId(userId);
                    p.setChannel(channel);
                    p.setCategory(category);
                    return p;
                });
        preference.setEnabled(enabled);
        return preferenceRepository.save(preference);
    }
}
