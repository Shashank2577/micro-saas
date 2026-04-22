package com.microsaas.ghostwriter.service;

import com.microsaas.ghostwriter.model.Template;
import com.microsaas.ghostwriter.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TemplateService {
    private final TemplateRepository repository;

    public List<Template> getAll(String tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public Optional<Template> getById(UUID id, String tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    @Transactional
    public Template create(Template template, String tenantId) {
        template.setTenantId(tenantId);
        return repository.save(template);
    }

    @Transactional
    public Template update(UUID id, Template template, String tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .map(existing -> {
                    existing.setName(template.getName());
                    existing.setStructure(template.getStructure());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Template not found"));
    }

    @Transactional
    public void delete(UUID id, String tenantId) {
        repository.findByIdAndTenantId(id, tenantId).ifPresent(repository::delete);
    }
}
