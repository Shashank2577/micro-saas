package com.microsaas.onboardflow.repository;
import com.microsaas.onboardflow.model.MilestoneChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface MilestoneChecklistRepository extends JpaRepository<MilestoneChecklist, UUID> {
    List<MilestoneChecklist> findByTenantId(UUID tenantId);
    Optional<MilestoneChecklist> findByIdAndTenantId(UUID id, UUID tenantId);
}
