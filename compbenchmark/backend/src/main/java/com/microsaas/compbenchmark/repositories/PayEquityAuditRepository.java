package com.microsaas.compbenchmark.repositories;

import com.microsaas.compbenchmark.model.PayEquityAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PayEquityAuditRepository extends JpaRepository<PayEquityAudit, UUID> {
    List<PayEquityAudit> findByGroupDimension(PayEquityAudit.GroupDimension groupDimension);
}
