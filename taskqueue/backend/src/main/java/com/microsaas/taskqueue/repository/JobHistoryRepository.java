package com.microsaas.taskqueue.repository;

import com.microsaas.taskqueue.domain.JobHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobHistoryRepository extends JpaRepository<JobHistory, UUID> {
    List<JobHistory> findByJobIdOrderByCreatedAtDesc(UUID jobId);
}
