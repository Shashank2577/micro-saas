package com.microsaas.onboardflow.repository;

import com.microsaas.onboardflow.model.BuddyPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BuddyPairRepository extends JpaRepository<BuddyPair, UUID> {
    List<BuddyPair> findByTenantId(UUID tenantId);
    Optional<BuddyPair> findByIdAndTenantId(UUID id, UUID tenantId);
}
