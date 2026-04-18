package com.microsaas.careerpath.repository;

import com.microsaas.careerpath.entity.CareerPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CareerPathRepository extends JpaRepository<CareerPath, UUID> {
    List<CareerPath> findAllByTenantId(String tenantId);
}
