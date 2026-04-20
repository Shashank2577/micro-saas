package com.microsaas.revopsai.repository;

import com.microsaas.revopsai.model.SalesVelocity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SalesVelocityRepository extends JpaRepository<SalesVelocity, UUID> {
    List<SalesVelocity> findByTenantId(UUID tenantId);
}
