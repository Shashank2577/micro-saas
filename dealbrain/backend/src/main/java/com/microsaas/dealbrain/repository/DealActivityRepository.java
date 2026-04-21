package com.microsaas.dealbrain.repository;

import com.microsaas.dealbrain.model.DealActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DealActivityRepository extends JpaRepository<DealActivity, UUID> {
    List<DealActivity> findByTenantIdAndDealId(UUID tenantId, UUID dealId);
}
