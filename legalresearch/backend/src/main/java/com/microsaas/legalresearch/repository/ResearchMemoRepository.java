package com.microsaas.legalresearch.repository;

import com.microsaas.legalresearch.domain.ResearchMemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResearchMemoRepository extends JpaRepository<ResearchMemo, UUID> {
    List<ResearchMemo> findByTenantId(UUID tenantId);

    @Query("SELECT m FROM ResearchMemo m WHERE m.tenantId = :tenantId AND (LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(m.content) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<ResearchMemo> searchMemos(@Param("query") String query, @Param("tenantId") UUID tenantId);
}
