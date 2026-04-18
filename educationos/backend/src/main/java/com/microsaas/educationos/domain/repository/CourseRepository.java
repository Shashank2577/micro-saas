package com.microsaas.educationos.domain.repository;

import com.microsaas.educationos.domain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    List<Course> findByTenantId(UUID tenantId);
    Optional<Course> findByIdAndTenantId(UUID id, UUID tenantId);
}
