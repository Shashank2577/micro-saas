package com.microsaas.documentvault.repository;

import com.microsaas.documentvault.model.StorageQuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StorageQuotaRepository extends JpaRepository<StorageQuota, UUID> {
}
