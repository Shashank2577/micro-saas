package com.microsaas.ghostwriter.repository;

import com.microsaas.ghostwriter.model.CorpusItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CorpusItemRepository extends JpaRepository<CorpusItem, UUID> {
    List<CorpusItem> findByVoiceModelIdAndTenantId(UUID voiceModelId, UUID tenantId);
    Optional<CorpusItem> findByIdAndTenantId(UUID id, UUID tenantId);
}
