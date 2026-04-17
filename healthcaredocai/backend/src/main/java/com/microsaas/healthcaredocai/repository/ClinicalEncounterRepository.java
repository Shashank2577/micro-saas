package com.microsaas.healthcaredocai.repository;

import com.microsaas.healthcaredocai.domain.ClinicalEncounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClinicalEncounterRepository extends JpaRepository<ClinicalEncounter, UUID> {
    List<ClinicalEncounter> findByTenantId(UUID tenantId);
    Optional<ClinicalEncounter> findByIdAndTenantId(UUID id, UUID tenantId);
}
