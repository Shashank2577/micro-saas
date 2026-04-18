package com.microsaas.marketsignal.repository;

import com.microsaas.marketsignal.domain.entity.InformationSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InformationSourceRepository extends JpaRepository<InformationSource, UUID> {
    List<InformationSource> findByTenantId(UUID tenantId);
}
