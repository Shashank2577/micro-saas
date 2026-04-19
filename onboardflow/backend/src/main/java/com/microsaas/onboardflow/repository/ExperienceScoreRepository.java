package com.microsaas.onboardflow.repository;
import com.microsaas.onboardflow.model.ExperienceScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface ExperienceScoreRepository extends JpaRepository<ExperienceScore, UUID> {
    List<ExperienceScore> findByTenantId(UUID tenantId);
    Optional<ExperienceScore> findByIdAndTenantId(UUID id, UUID tenantId);
}
