package com.microsaas.knowledgevault.repository;

import com.microsaas.knowledgevault.domain.KnowledgeSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KnowledgeSourceRepository extends JpaRepository<KnowledgeSource, UUID> {
    List<KnowledgeSource> findByTenantId(String tenantId);
}
