package com.micro.interviewos.repository;

import com.micro.interviewos.domain.Scorecard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScorecardRepository extends JpaRepository<Scorecard, UUID> {
    List<Scorecard> findByTenantId(UUID tenantId);
    Optional<Scorecard> findByIdAndTenantId(UUID id, UUID tenantId);
}
