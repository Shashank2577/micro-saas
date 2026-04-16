package com.microsaas.runwaymodeler.repository;

import com.microsaas.runwaymodeler.model.RunwayModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RunwayModelRepository extends JpaRepository<RunwayModel, UUID> {
    List<RunwayModel> findByTenantId(UUID tenantId);
}
