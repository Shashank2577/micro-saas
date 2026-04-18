package com.microsaas.meetingbrain.repository;

import com.microsaas.meetingbrain.model.Decision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DecisionRepository extends JpaRepository<Decision, UUID> {
    List<Decision> findByTenantIdAndMeetingId(UUID tenantId, UUID meetingId);
    List<Decision> findByTenantId(UUID tenantId);
    
    @Query(value = "SELECT * FROM decisions d WHERE d.tenant_id = :tenantId ORDER BY d.embedding <-> cast(:embedding as vector) LIMIT :limit", nativeQuery = true)
    List<Decision> searchByEmbedding(@Param("tenantId") UUID tenantId, @Param("embedding") String embeddingStr, @Param("limit") int limit);
}
