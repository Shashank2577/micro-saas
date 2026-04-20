package com.microsaas.supportintelligence.repository;

import com.microsaas.supportintelligence.model.ProductIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductIssueRepository extends JpaRepository<ProductIssue, UUID> {
    List<ProductIssue> findByTenantId(UUID tenantId);
    Optional<ProductIssue> findByIdAndTenantId(UUID id, UUID tenantId);
}
