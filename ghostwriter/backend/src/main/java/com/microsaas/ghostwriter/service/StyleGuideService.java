package com.microsaas.ghostwriter.service;

import com.microsaas.ghostwriter.model.StyleGuide;
import com.microsaas.ghostwriter.repository.StyleGuideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StyleGuideService {
    private final StyleGuideRepository repository;

    public List<StyleGuide> getAll(String tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public Optional<StyleGuide> getById(UUID id, String tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    @Transactional
    public StyleGuide create(StyleGuide styleGuide, String tenantId) {
        styleGuide.setTenantId(tenantId);
        return repository.save(styleGuide);
    }

    @Transactional
    public StyleGuide update(UUID id, StyleGuide styleGuide, String tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .map(existing -> {
                    existing.setName(styleGuide.getName());
                    existing.setRules(styleGuide.getRules());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("StyleGuide not found"));
    }

    @Transactional
    public void delete(UUID id, String tenantId) {
        repository.findByIdAndTenantId(id, tenantId).ifPresent(repository::delete);
    }
}
