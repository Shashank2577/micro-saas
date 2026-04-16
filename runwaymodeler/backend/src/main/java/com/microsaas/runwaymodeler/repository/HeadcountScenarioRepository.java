package com.microsaas.runwaymodeler.repository;

import com.microsaas.runwaymodeler.model.HeadcountScenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HeadcountScenarioRepository extends JpaRepository<HeadcountScenario, UUID> {
    List<HeadcountScenario> findByModelIdAndTenantId(UUID modelId, UUID tenantId);
}
