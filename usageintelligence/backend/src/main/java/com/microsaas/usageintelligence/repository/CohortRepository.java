package com.microsaas.usageintelligence.repository;

import com.microsaas.usageintelligence.domain.Cohort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CohortRepository extends JpaRepository<Cohort, UUID> {
    List<Cohort> findByTenantId(UUID tenantId);
}
