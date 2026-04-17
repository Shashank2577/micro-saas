package com.microsaas.notificationhub.service;

import com.microsaas.notificationhub.domain.entity.Notification;
import com.microsaas.notificationhub.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class SchedulingService {

    private final NotificationRepository notificationRepository;
    private final DeliveryService deliveryService;

    @Scheduled(fixedRate = 60000) // Run every minute
    @Transactional
    public void processScheduledNotifications() {
        log.debug("Checking for scheduled notifications...");
        List<Notification> dueNotifications = notificationRepository
                .findByStatusAndScheduledForBefore("SCHEDULED", ZonedDateTime.now());

        for (Notification notification : dueNotifications) {
            log.info("Processing scheduled notification {}", notification.getId());
            deliveryService.deliverNotification(notification);
        }
    }
}
