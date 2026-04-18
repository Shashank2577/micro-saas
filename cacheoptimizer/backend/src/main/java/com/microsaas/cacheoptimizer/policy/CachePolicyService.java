package com.microsaas.cacheoptimizer.policy;

import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CachePolicyService {

    private final CachePolicyRepository repository;

    public CachePolicyService(CachePolicyRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public CachePolicyDto createPolicy(CachePolicyDto dto) {
        String tenantId = TenantContext.require().toString();
        CachePolicy policy = new CachePolicy();
        policy.setTenantId(tenantId);
        mapDtoToEntity(dto, policy);
        CachePolicy saved = repository.save(policy);
        return mapEntityToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<CachePolicyDto> getPolicies() {
        String tenantId = TenantContext.require().toString();
        return repository.findByTenantId(tenantId).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public CachePolicy getPolicyEntity(String namespace) {
        String tenantId = TenantContext.require().toString();
        return repository.findByTenantIdAndNamespace(tenantId, namespace).orElse(null);
    }

    @Transactional
    public CachePolicyDto updatePolicy(UUID id, CachePolicyDto dto) {
        String tenantId = TenantContext.require().toString();
        CachePolicy policy = repository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
        mapDtoToEntity(dto, policy);
        CachePolicy saved = repository.save(policy);
        return mapEntityToDto(saved);
    }

    @Transactional
    public void deletePolicy(UUID id) {
        String tenantId = TenantContext.require().toString();
        CachePolicy policy = repository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
        repository.delete(policy);
    }

    private void mapDtoToEntity(CachePolicyDto dto, CachePolicy entity) {
        entity.setAppName(dto.getAppName());
        entity.setNamespace(dto.getNamespace());
        entity.setTtlSeconds(dto.getTtlSeconds());
        entity.setStrategy(dto.getStrategy());
        entity.setCompressionEnabled(dto.getCompressionEnabled() != null ? dto.getCompressionEnabled() : false);
        entity.setStaleWhileRevalidate(dto.getStaleWhileRevalidate() != null ? dto.getStaleWhileRevalidate() : false);
        entity.setStaleTtlSeconds(dto.getStaleTtlSeconds());
    }

    private CachePolicyDto mapEntityToDto(CachePolicy entity) {
        CachePolicyDto dto = new CachePolicyDto();
        dto.setId(entity.getId());
        dto.setAppName(entity.getAppName());
        dto.setNamespace(entity.getNamespace());
        dto.setTtlSeconds(entity.getTtlSeconds());
        dto.setStrategy(entity.getStrategy());
        dto.setCompressionEnabled(entity.getCompressionEnabled());
        dto.setStaleWhileRevalidate(entity.getStaleWhileRevalidate());
        dto.setStaleTtlSeconds(entity.getStaleTtlSeconds());
        return dto;
    }
}
