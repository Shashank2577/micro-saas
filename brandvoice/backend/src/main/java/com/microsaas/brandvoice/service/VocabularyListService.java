package com.microsaas.brandvoice.service;

import com.microsaas.brandvoice.entity.VocabularyList;
import com.microsaas.brandvoice.repository.VocabularyListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VocabularyListService {
    private final VocabularyListRepository repository;

    public List<VocabularyList> findAllByTenant(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public VocabularyList save(VocabularyList entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
