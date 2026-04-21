package com.microsaas.brandvoice.repository;

import com.microsaas.brandvoice.entity.VocabularyList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VocabularyListRepository extends JpaRepository<VocabularyList, UUID> {
    List<VocabularyList> findByTenantId(UUID tenantId);
}
