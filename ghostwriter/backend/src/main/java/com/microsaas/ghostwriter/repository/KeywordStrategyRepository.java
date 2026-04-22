package com.microsaas.ghostwriter.repository;

import com.microsaas.ghostwriter.model.KeywordStrategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface KeywordStrategyRepository extends JpaRepository<KeywordStrategy, UUID> {
    List<KeywordStrategy> findByTenantId(String tenantId);
    Optional<KeywordStrategy> findByIdAndTenantId(UUID id, String tenantId);
}
