package com.microsaas.notificationhub.domain.repository;

import com.microsaas.notificationhub.domain.entity.NotificationDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationDeliveryRepository extends JpaRepository<NotificationDelivery, UUID> {
    List<NotificationDelivery> findByTenantId(String tenantId);
    Optional<NotificationDelivery> findByIdAndTenantId(UUID id, String tenantId);
    List<NotificationDelivery> findByNotificationIdAndTenantId(UUID notificationId, String tenantId);
}
