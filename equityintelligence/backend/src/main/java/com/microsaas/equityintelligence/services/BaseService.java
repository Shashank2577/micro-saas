package com.microsaas.equityintelligence.services;

import com.microsaas.equityintelligence.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.crosscutting.starter.tenancy.TenantContext;

public abstract class BaseService<T extends BaseEntity, R extends JpaRepository<T, UUID>> {

    protected final R repository;

    protected BaseService(R repository) {
        this.repository = repository;
    }

    protected abstract List<T> findByTenantId(UUID tenantId);
    protected abstract Optional<T> findByIdAndTenantId(UUID id, UUID tenantId);

    public List<T> findAll() {
        return findByTenantId(TenantContext.require());
    }

    public Optional<T> findById(UUID id) {
        return findByIdAndTenantId(id, TenantContext.require());
    }

    public T create(T entity) {
        entity.setTenantId(TenantContext.require());
        T saved = repository.save(entity);
        onAfterSave(saved, true);
        return saved;
    }

    public T update(UUID id, T entityDetails) {
        T entity = findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Entity not found"));

        entity.setName(entityDetails.getName());
        entity.setStatus(entityDetails.getStatus());
        entity.setMetadataJson(entityDetails.getMetadataJson());

        T saved = repository.save(entity);
        onAfterSave(saved, false);
        return saved;
    }

    public void delete(UUID id) {
        T entity = findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Entity not found"));
        repository.delete(entity);
    }

    protected void onAfterSave(T entity, boolean isCreate) {
        // Subclasses can override
    }
}
