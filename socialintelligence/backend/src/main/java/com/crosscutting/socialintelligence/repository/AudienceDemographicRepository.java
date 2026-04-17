package com.crosscutting.socialintelligence.repository;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import com.crosscutting.socialintelligence.domain.AudienceDemographic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AudienceDemographicRepository extends JpaRepository<AudienceDemographic, UUID> {
    List<AudienceDemographic> findByTenantId(String tenantId);
    List<AudienceDemographic> findByTenantIdAndAccountPlatformName(String tenantId, String platformName);
}
