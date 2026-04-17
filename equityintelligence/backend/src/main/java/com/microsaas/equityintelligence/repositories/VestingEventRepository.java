package com.microsaas.equityintelligence.repositories;

import com.microsaas.equityintelligence.model.VestingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VestingEventRepository extends JpaRepository<VestingEvent, UUID> {
    List<VestingEvent> findAllByGrantIdAndTenantIdOrderByVestDateAsc(UUID grantId, UUID tenantId);
    void deleteAllByGrantIdAndTenantId(UUID grantId, UUID tenantId);
}
