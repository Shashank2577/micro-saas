package com.microsaas.creatoranalytics.repository;

import com.microsaas.creatoranalytics.model.ContentChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContentChannelRepository extends JpaRepository<ContentChannel, UUID> {
    List<ContentChannel> findByTenantId(UUID tenantId);
    Optional<ContentChannel> findByIdAndTenantId(UUID id, UUID tenantId);
}
