package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.HeadcountPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HeadcountPlanRepository extends JpaRepository<HeadcountPlan, UUID> {
    List<HeadcountPlan> findByTenantId(UUID tenantId);
    Optional<HeadcountPlan> findByIdAndTenantId(UUID id, UUID tenantId);
}
