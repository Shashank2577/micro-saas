package com.microsaas.nonprofitos.repository;

import com.microsaas.nonprofitos.domain.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DonorRepository extends JpaRepository<Donor, UUID> {
    List<Donor> findByTenantId(UUID tenantId);
    Optional<Donor> findByIdAndTenantId(UUID id, UUID tenantId);
}
