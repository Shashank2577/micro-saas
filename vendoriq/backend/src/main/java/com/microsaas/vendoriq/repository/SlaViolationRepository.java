package com.microsaas.vendoriq.repository;

import com.microsaas.vendoriq.model.SlaViolation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SlaViolationRepository extends JpaRepository<SlaViolation, UUID> {
    List<SlaViolation> findByVendorId(UUID vendorId);
}
