package com.microsaas.notificationhub.domain.repository;

import com.microsaas.notificationhub.domain.entity.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, UUID> {
    List<NotificationTemplate> findByTenantId(String tenantId);
    Optional<NotificationTemplate> findByIdAndTenantId(UUID id, String tenantId);
}
