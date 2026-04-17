package com.microsaas.datastoryteller.repository;

import com.microsaas.datastoryteller.domain.model.ScheduledDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduledDeliveryRepository extends JpaRepository<ScheduledDelivery, UUID> {
    List<ScheduledDelivery> findByTenantId(String tenantId);
    Optional<ScheduledDelivery> findByIdAndTenantId(UUID id, String tenantId);
    void deleteByIdAndTenantId(UUID id, String tenantId);
}
