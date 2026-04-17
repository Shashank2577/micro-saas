package com.crosscutting.starter.notifications;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    Page<Notification> findByUserIdAndTenantId(UUID userId, UUID tenantId, Pageable pageable);

    long countByUserIdAndTenantIdAndReadAtIsNull(UUID userId, UUID tenantId);
}
