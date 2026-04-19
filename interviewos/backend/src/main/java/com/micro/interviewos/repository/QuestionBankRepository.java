package com.micro.interviewos.repository;

import com.micro.interviewos.domain.QuestionBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionBankRepository extends JpaRepository<QuestionBank, UUID> {
    List<QuestionBank> findByTenantId(UUID tenantId);
    Optional<QuestionBank> findByIdAndTenantId(UUID id, UUID tenantId);
}
