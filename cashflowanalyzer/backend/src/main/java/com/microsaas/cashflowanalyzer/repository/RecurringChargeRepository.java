package com.microsaas.cashflowanalyzer.repository;

import com.microsaas.cashflowanalyzer.model.RecurringCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecurringChargeRepository extends JpaRepository<RecurringCharge, UUID> {
    List<RecurringCharge> findByTenantId(String tenantId);
}
