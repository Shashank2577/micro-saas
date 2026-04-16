package com.microsaas.equityintelligence.repositories;

import com.microsaas.equityintelligence.model.FundingRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FundingRoundRepository extends JpaRepository<FundingRound, UUID> {
    List<FundingRound> findAllByTenantIdOrderByClosedAtAsc(UUID tenantId);
}
