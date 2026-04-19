package com.micro.interviewos.service;

import com.micro.interviewos.domain.QuestionBank;
import com.micro.interviewos.dto.QuestionBankDTO;
import com.micro.interviewos.repository.QuestionBankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionBankService {

    private final QuestionBankRepository repository;

    public List<QuestionBankDTO> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<QuestionBankDTO> findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .map(this::toDto);
    }

    public QuestionBankDTO create(QuestionBankDTO dto) {
        QuestionBank entity = toEntity(dto);
        entity = repository.save(entity);
        return toDto(entity);
    }

    public QuestionBankDTO update(UUID id, UUID tenantId, QuestionBankDTO dto) {
        QuestionBank entity = repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Not found"));
        entity.setName(dto.getName());
        entity.setStatus(dto.getStatus());
        entity.setMetadataJson(dto.getMetadataJson());
        return toDto(repository.save(entity));
    }

    public void validate(UUID id, UUID tenantId) {
        repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Not found"));
        // validation logic placeholder
    }

    private QuestionBankDTO toDto(QuestionBank entity) {
        QuestionBankDTO dto = new QuestionBankDTO();
        dto.setId(entity.getId());
        dto.setTenantId(entity.getTenantId());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus());
        dto.setMetadataJson(entity.getMetadataJson());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private QuestionBank toEntity(QuestionBankDTO dto) {
        QuestionBank entity = new QuestionBank();
        entity.setId(dto.getId());
        entity.setTenantId(dto.getTenantId());
        entity.setName(dto.getName());
        entity.setStatus(dto.getStatus());
        entity.setMetadataJson(dto.getMetadataJson());
        return entity;
    }
}
