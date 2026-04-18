package com.microsaas.debtnavigator.repository;

import com.microsaas.debtnavigator.entity.ConsolidationOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConsolidationOfferRepository extends JpaRepository<ConsolidationOffer, UUID> {
    List<ConsolidationOffer> findByTenantId(UUID tenantId);
    Optional<ConsolidationOffer> findByIdAndTenantId(UUID id, UUID tenantId);
}
