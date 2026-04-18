package com.microsaas.customersuccessos.repository;

import com.microsaas.customersuccessos.model.QbrDeck;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface QbrDeckRepository extends JpaRepository<QbrDeck, UUID> {
    List<QbrDeck> findByTenantIdAndAccountIdOrderByGeneratedAtDesc(UUID tenantId, UUID accountId);
}
