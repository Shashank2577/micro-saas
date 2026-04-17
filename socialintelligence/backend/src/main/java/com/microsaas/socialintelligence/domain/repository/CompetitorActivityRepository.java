package com.microsaas.socialintelligence.domain.repository;

import com.microsaas.socialintelligence.domain.model.CompetitorActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompetitorActivityRepository extends JpaRepository<CompetitorActivity, UUID> {
    List<CompetitorActivity> findByCompetitorHandle(String competitorHandle);
}
