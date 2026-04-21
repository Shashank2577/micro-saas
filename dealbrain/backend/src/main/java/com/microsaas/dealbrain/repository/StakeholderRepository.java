package com.microsaas.dealbrain.repository;

import com.microsaas.dealbrain.model.Stakeholder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StakeholderRepository extends JpaRepository<Stakeholder, UUID> {
    List<Stakeholder> findByTenantIdAndDealId(UUID tenantId, UUID dealId);
}
