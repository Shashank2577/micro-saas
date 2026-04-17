package com.microsaas.jobcraftai.repository;

import com.microsaas.jobcraftai.model.JobVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobVariantRepository extends JpaRepository<JobVariant, UUID> {
    List<JobVariant> findByJobId(UUID jobId);
}
