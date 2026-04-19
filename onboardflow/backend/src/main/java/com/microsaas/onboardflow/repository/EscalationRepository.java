package com.microsaas.onboardflow.repository;
import com.microsaas.onboardflow.model.Escalation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface EscalationRepository extends JpaRepository<Escalation, UUID> {
    List<Escalation> findByTenantId(UUID tenantId);
    Optional<Escalation> findByIdAndTenantId(UUID id, UUID tenantId);
}
