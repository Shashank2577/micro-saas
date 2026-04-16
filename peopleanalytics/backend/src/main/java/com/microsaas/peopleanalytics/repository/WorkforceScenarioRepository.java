package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.WorkforceScenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkforceScenarioRepository extends JpaRepository<WorkforceScenario, UUID> {
    List<WorkforceScenario> findByTenantId(UUID tenantId);
    Optional<WorkforceScenario> findByIdAndTenantId(UUID id, UUID tenantId);
}
