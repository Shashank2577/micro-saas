package com.microsaas.ghostwriter.service;

import com.microsaas.ghostwriter.model.Persona;
import com.microsaas.ghostwriter.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonaService {
    private final PersonaRepository repository;

    public List<Persona> getAll(String tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public Optional<Persona> getById(UUID id, String tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    @Transactional
    public Persona create(Persona persona, String tenantId) {
        persona.setTenantId(tenantId);
        return repository.save(persona);
    }

    @Transactional
    public Persona update(UUID id, Persona persona, String tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .map(existing -> {
                    existing.setName(persona.getName());
                    existing.setDescription(persona.getDescription());
                    existing.setTone(persona.getTone());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Persona not found"));
    }

    @Transactional
    public void delete(UUID id, String tenantId) {
        repository.findByIdAndTenantId(id, tenantId).ifPresent(repository::delete);
    }
}
