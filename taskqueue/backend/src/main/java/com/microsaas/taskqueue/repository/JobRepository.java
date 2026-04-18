package com.microsaas.taskqueue.repository;

import com.microsaas.taskqueue.domain.Job;
import com.microsaas.taskqueue.domain.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {
    List<Job> findByTenantId(UUID tenantId);
    List<Job> findByTenantIdAndStatus(UUID tenantId, JobStatus status);

    @Query("SELECT j FROM Job j WHERE j.status = :status AND (j.nextRunAt IS NULL OR j.nextRunAt <= :now) ORDER BY CASE j.priority WHEN 'CRITICAL' THEN 4 WHEN 'HIGH' THEN 3 WHEN 'NORMAL' THEN 2 WHEN 'LOW' THEN 1 END DESC, j.createdAt ASC")
    List<Job> findReadyJobs(@Param("status") JobStatus status, @Param("now") Instant now);

    @Query("SELECT j FROM Job j WHERE j.status = 'RUNNING'")
    List<Job> findTimedOutJobs(@Param("timeoutThreshold") Instant timeoutThreshold);

    long countByTenantIdAndStatus(UUID tenantId, JobStatus status);
    long countByTenantId(UUID tenantId);
}
