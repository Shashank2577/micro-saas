package com.microsaas.contractportfolio.repository;

import com.microsaas.contractportfolio.domain.CounterpartyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CounterpartyProfileRepository extends JpaRepository<CounterpartyProfile, UUID> {
    List<CounterpartyProfile> findAllByTenantId(UUID tenantId);
    Optional<CounterpartyProfile> findByIdAndTenantId(UUID id, UUID tenantId);
}
