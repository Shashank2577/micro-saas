package com.microsaas.educationos.domain.repository;

import com.microsaas.educationos.domain.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
    List<Question> findByQuizIdAndTenantId(UUID quizId, UUID tenantId);
}
