package com.microsaas.ghostwriter.repository;

import com.microsaas.ghostwriter.model.VoiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VoiceModelRepository extends JpaRepository<VoiceModel, UUID> {
    List<VoiceModel> findByTenantId(UUID tenantId);
    Optional<VoiceModel> findByIdAndTenantId(UUID id, UUID tenantId);
}
