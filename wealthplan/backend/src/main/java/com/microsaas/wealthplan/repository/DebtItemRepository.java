package com.microsaas.wealthplan.repository;

import com.microsaas.wealthplan.entity.DebtItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DebtItemRepository extends JpaRepository<DebtItem, UUID> {
    List<DebtItem> findByTenantId(String tenantId);
}
