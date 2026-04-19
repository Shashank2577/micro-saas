package com.microsaas.onboardflow.repository;
import com.microsaas.onboardflow.model.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, UUID> {
    List<TaskAssignment> findByTenantId(UUID tenantId);
    Optional<TaskAssignment> findByIdAndTenantId(UUID id, UUID tenantId);
}
