package com.microsaas.agencyos.repository;

import com.microsaas.agencyos.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    List<Client> findByTenantId(String tenantId);
    Optional<Client> findByIdAndTenantId(UUID id, String tenantId);
}
