package com.crosscutting.socialintelligence.repository;

import com.crosscutting.socialintelligence.domain.AudienceDemographic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AudienceDemographicRepository extends JpaRepository<AudienceDemographic, UUID> {
    List<AudienceDemographic> findByTenantId(UUID tenantId);
}
