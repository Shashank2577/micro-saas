package com.microsaas.equityintelligence.repositories;

import com.microsaas.equityintelligence.model.Stakeholder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StakeholderRepository extends JpaRepository<Stakeholder, UUID> {
    List<Stakeholder> findAllByTenantId(UUID tenantId);
}
