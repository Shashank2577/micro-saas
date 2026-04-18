package com.crosscutting.socialintelligence.repository;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;





import com.crosscutting.socialintelligence.domain.GrowthRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GrowthRecommendationRepository extends JpaRepository<GrowthRecommendation, UUID> {
    List<GrowthRecommendation> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    Optional<GrowthRecommendation> findByIdAndTenantId(UUID id, String tenantId);
}
