package com.microsaas.educationos.domain.repository;

import com.microsaas.educationos.domain.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, UUID> {
    List<Quiz> findByModuleIdAndTenantId(UUID moduleId, UUID tenantId);
    Optional<Quiz> findByIdAndTenantId(UUID id, UUID tenantId);
}
