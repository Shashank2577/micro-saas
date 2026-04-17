package com.microsaas.creatoranalytics.repository;

import com.microsaas.creatoranalytics.model.BusinessOutcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BusinessOutcomeRepository extends JpaRepository<BusinessOutcome, UUID> {
    List<BusinessOutcome> findByTenantId(UUID tenantId);
    List<BusinessOutcome> findByTenantIdAndChannelId(UUID tenantId, UUID channelId);
}
