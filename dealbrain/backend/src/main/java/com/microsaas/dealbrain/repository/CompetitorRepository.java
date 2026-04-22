package com.microsaas.dealbrain.repository;

import com.microsaas.dealbrain.model.Competitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompetitorRepository extends JpaRepository<Competitor, UUID> {
    List<Competitor> findByTenantIdAndDealId(UUID tenantId, UUID dealId);
}
