package com.microsaas.dataunification.service;

import com.microsaas.dataunification.model.SchemaMapping;
import com.microsaas.dataunification.repository.SchemaMappingRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
public class SchemaMappingService {
    private final SchemaMappingRepository repository;

    public SchemaMappingService(SchemaMappingRepository repository) {
        this.repository = repository;
    }

    public List<SchemaMapping> findAll() {
        return repository.findByTenantId(TenantContext.require());
    }

    public SchemaMapping findById(UUID id) {
        return repository.findById(id).filter(m -> m.getTenantId().equals(TenantContext.require())).orElseThrow();
    }

    public SchemaMapping create(SchemaMapping mapping) {
        mapping.setId(UUID.randomUUID());
        mapping.setTenantId(TenantContext.require());
        mapping.setCreatedAt(LocalDateTime.now());
        mapping.setUpdatedAt(LocalDateTime.now());
        return repository.save(mapping);
    }
    
    public String suggestMapping(String sourceSchema, String targetSchema) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer sk-local-dev-key");

        String requestJson = String.format("{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"Suggest mapping for source: %s to target: %s\"}]}", sourceSchema, targetSchema);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity("http://litellm:4000/v1/chat/completions", entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            return "{\"error\": \"LiteLLM suggestion failed. Please check connection.\"}";
        }
    }
}
