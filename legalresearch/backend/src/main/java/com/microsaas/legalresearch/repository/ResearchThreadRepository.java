package com.microsaas.legalresearch.repository;

import com.microsaas.legalresearch.domain.ResearchThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResearchThreadRepository extends JpaRepository<ResearchThread, UUID> {
    List<ResearchThread> findByTenantId(UUID tenantId);
}
