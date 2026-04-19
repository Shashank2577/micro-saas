package com.microsaas.onboardflow.repository;
import com.microsaas.onboardflow.model.CompletionSignal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface CompletionSignalRepository extends JpaRepository<CompletionSignal, UUID> {
    List<CompletionSignal> findByTenantId(UUID tenantId);
    Optional<CompletionSignal> findByIdAndTenantId(UUID id, UUID tenantId);
}
