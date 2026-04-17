package com.crosscutting.starter.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InAppChannel implements NotificationChannel {

    private static final Logger log = LoggerFactory.getLogger(InAppChannel.class);

    private final NotificationRepository notificationRepository;

    public InAppChannel(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void send(Notification notification) {
        notificationRepository.save(notification);
        log.info("In-app notification saved for user {} in tenant {}: {}",
                notification.getUserId(), notification.getTenantId(), notification.getTitle());
    }

    @Override
    public String channelName() {
        return "in_app";
    }
}
