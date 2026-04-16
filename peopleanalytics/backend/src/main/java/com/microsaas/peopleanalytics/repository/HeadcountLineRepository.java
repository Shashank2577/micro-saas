package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.HeadcountLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HeadcountLineRepository extends JpaRepository<HeadcountLine, UUID> {
    List<HeadcountLine> findByPlanIdAndTenantId(UUID planId, UUID tenantId);
}
