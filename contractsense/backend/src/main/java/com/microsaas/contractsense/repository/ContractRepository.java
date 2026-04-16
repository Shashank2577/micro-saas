package com.microsaas.contractsense.repository;

import com.microsaas.contractsense.domain.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContractRepository extends JpaRepository<Contract, UUID> {
    List<Contract> findByTenantId(UUID tenantId);
    Optional<Contract> findByIdAndTenantId(UUID id, UUID tenantId);
}
