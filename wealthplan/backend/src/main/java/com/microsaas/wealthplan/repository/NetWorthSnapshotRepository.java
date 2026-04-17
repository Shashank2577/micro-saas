package com.microsaas.wealthplan.repository;

import com.microsaas.wealthplan.entity.NetWorthSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NetWorthSnapshotRepository extends JpaRepository<NetWorthSnapshot, UUID> {
    List<NetWorthSnapshot> findByTenantIdOrderBySnapshotDateDesc(String tenantId);
}
