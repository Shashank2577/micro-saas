package com.microsaas.meetingbrain.repository;

import com.microsaas.meetingbrain.model.ActionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActionItemRepository extends JpaRepository<ActionItem, UUID> {
    List<ActionItem> findByTenantIdAndMeetingId(UUID tenantId, UUID meetingId);
    List<ActionItem> findByTenantId(UUID tenantId);
    List<ActionItem> findByTenantIdAndStatus(UUID tenantId, String status);
}
