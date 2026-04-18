package com.microsaas.taskqueue.repository;

import com.microsaas.taskqueue.domain.ScheduledTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduledTaskRepository extends JpaRepository<ScheduledTask, UUID> {
    List<ScheduledTask> findByTenantId(UUID tenantId);
    List<ScheduledTask> findByActiveTrue();
}
