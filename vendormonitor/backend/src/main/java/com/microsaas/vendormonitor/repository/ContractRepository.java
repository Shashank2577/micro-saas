package com.microsaas.vendormonitor.repository;

import com.microsaas.vendormonitor.domain.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContractRepository extends JpaRepository<Contract, UUID> {
    List<Contract> findByTenantId(UUID tenantId);
    List<Contract> findByTenantIdAndVendorId(UUID tenantId, UUID vendorId);
    Optional<Contract> findByIdAndTenantId(UUID id, UUID tenantId);
}
