package com.microsaas.brandvoice.service;

import com.microsaas.brandvoice.entity.ContentProject;
import com.microsaas.brandvoice.repository.ContentProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContentProjectService {
    private final ContentProjectRepository repository;

    public List<ContentProject> findAllByTenant(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public ContentProject save(ContentProject entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
