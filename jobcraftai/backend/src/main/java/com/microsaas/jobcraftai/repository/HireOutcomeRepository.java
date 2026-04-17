package com.microsaas.jobcraftai.repository;

import com.microsaas.jobcraftai.model.HireOutcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HireOutcomeRepository extends JpaRepository<HireOutcome, UUID> {
    List<HireOutcome> findByJobId(UUID jobId);
}
