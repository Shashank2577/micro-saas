package com.microsaas.revopsai.repository;

import com.microsaas.revopsai.model.LtvModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LtvModelRepository extends JpaRepository<LtvModel, UUID> {
    List<LtvModel> findByTenantId(UUID tenantId);
}
