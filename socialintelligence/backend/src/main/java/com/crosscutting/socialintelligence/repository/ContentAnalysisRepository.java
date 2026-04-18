package com.crosscutting.socialintelligence.repository;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;





import com.crosscutting.socialintelligence.domain.ContentAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContentAnalysisRepository extends JpaRepository<ContentAnalysis, UUID> {
    List<ContentAnalysis> findByTenantIdOrderByLikesDesc(String tenantId);
    List<ContentAnalysis> findByTenantIdAndAccountPlatformNameOrderByLikesDesc(String tenantId, String platformName);
    Optional<ContentAnalysis> findByIdAndTenantId(UUID id, String tenantId);
}
