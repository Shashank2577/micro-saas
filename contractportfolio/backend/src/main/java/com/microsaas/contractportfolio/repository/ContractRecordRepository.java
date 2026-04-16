package com.microsaas.contractportfolio.repository;

import com.microsaas.contractportfolio.domain.ContractRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ContractRecordRepository extends JpaRepository<ContractRecord, UUID> {
    List<ContractRecord> findByTenantId(UUID tenantId);
    List<ContractRecord> findByTenantIdAndEndDateBetween(UUID tenantId, LocalDate startDate, LocalDate endDate);
}
