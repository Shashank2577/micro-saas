package com.microsaas.cashflowanalyzer.repository;

import com.microsaas.cashflowanalyzer.model.CashFlowSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CashFlowSnapshotRepository extends JpaRepository<CashFlowSnapshot, UUID> {
    List<CashFlowSnapshot> findByTenantId(String tenantId);
}
