package com.microsaas.procurebot.repository;

import com.microsaas.procurebot.model.VendorOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VendorOfferRepository extends JpaRepository<VendorOffer, UUID> {
    List<VendorOffer> findByTenantId(UUID tenantId);
    Optional<VendorOffer> findByIdAndTenantId(UUID id, UUID tenantId);
}
