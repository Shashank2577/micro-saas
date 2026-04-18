package com.microsaas.prospectiq.repository;

import com.microsaas.prospectiq.model.Signal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SignalRepository extends JpaRepository<Signal, UUID> {
    List<Signal> findByTenantIdAndProspectIdOrderByDetectedAtDesc(UUID tenantId, UUID prospectId);
}
