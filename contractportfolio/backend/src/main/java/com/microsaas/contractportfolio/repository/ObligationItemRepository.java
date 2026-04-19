package com.microsaas.contractportfolio.repository;

import com.microsaas.contractportfolio.domain.ObligationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ObligationItemRepository extends JpaRepository<ObligationItem, UUID> {
    List<ObligationItem> findAllByTenantId(UUID tenantId);
    Optional<ObligationItem> findByIdAndTenantId(UUID id, UUID tenantId);
}
