package com.microsaas.notificationhub.domain.repository;

import com.microsaas.notificationhub.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByTenantId(String tenantId);
    Optional<Notification> findByIdAndTenantId(UUID id, String tenantId);
    List<Notification> findByStatusAndScheduledForBefore(String status, ZonedDateTime time);
}
