package com.microsaas.contractportfolio.repository;

import com.microsaas.contractportfolio.domain.ContractRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContractRecordRepository extends JpaRepository<ContractRecord, UUID> {
    List<ContractRecord> findAllByTenantId(UUID tenantId);
    Optional<ContractRecord> findByIdAndTenantId(UUID id, UUID tenantId);
}
