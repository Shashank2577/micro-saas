package com.microsaas.dataroomai.repository;

import com.microsaas.dataroomai.domain.AIAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AIAnswerRepository extends JpaRepository<AIAnswer, UUID> {
    List<AIAnswer> findByRoomIdAndTenantId(UUID roomId, UUID tenantId);
    Optional<AIAnswer> findByIdAndTenantId(UUID id, UUID tenantId);
}
