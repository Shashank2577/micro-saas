package com.microsaas.pricingintelligence.repository;

import com.microsaas.pricingintelligence.domain.CustomerSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerSegmentRepository extends JpaRepository<CustomerSegment, UUID> {
    List<CustomerSegment> findByTenantId(UUID tenantId);
}
