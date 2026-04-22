package com.microsaas.ghostwriter.repository;

import com.microsaas.ghostwriter.model.ContentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContentRequestRepository extends JpaRepository<ContentRequest, UUID> {
    List<ContentRequest> findByTenantId(String tenantId);
    Optional<ContentRequest> findByIdAndTenantId(UUID id, String tenantId);
}
