package com.microsaas.ghostwriter.repository;

import com.microsaas.ghostwriter.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, UUID> {
    List<Persona> findByTenantId(String tenantId);
    Optional<Persona> findByIdAndTenantId(UUID id, String tenantId);
}
