package com.microsaas.legalresearch.repository;

import com.microsaas.legalresearch.domain.Citation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CitationRepository extends JpaRepository<Citation, UUID> {
    List<Citation> findByMemoIdAndTenantId(UUID memoId, UUID tenantId);
}
