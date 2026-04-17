package com.microsaas.knowledgevault.repository;

import com.microsaas.knowledgevault.domain.KnowledgeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KnowledgeQueryRepository extends JpaRepository<KnowledgeQuery, UUID> {
    List<KnowledgeQuery> findByTenantIdOrderByFrequencyDesc(String tenantId);
}
