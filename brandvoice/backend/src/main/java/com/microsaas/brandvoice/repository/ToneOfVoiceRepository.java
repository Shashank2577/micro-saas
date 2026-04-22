package com.microsaas.brandvoice.repository;

import com.microsaas.brandvoice.entity.ToneOfVoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ToneOfVoiceRepository extends JpaRepository<ToneOfVoice, UUID> {
    List<ToneOfVoice> findByTenantId(UUID tenantId);
}
