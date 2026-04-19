package com.micro.interviewos.service;

import com.micro.interviewos.domain.EvaluationRecord;
import com.micro.interviewos.dto.EvaluationRecordDTO;
import com.micro.interviewos.repository.EvaluationRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvaluationRecordService {

    private final EvaluationRecordRepository repository;

    public List<EvaluationRecordDTO> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<EvaluationRecordDTO> findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .map(this::toDto);
    }

    public EvaluationRecordDTO create(EvaluationRecordDTO dto) {
        EvaluationRecord entity = toEntity(dto);
        entity = repository.save(entity);
        return toDto(entity);
    }

    public EvaluationRecordDTO update(UUID id, UUID tenantId, EvaluationRecordDTO dto) {
        EvaluationRecord entity = repository.findByIdAndTenantId(id, tenantId)
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

    private EvaluationRecordDTO toDto(EvaluationRecord entity) {
        EvaluationRecordDTO dto = new EvaluationRecordDTO();
        dto.setId(entity.getId());
        dto.setTenantId(entity.getTenantId());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus());
        dto.setMetadataJson(entity.getMetadataJson());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private EvaluationRecord toEntity(EvaluationRecordDTO dto) {
        EvaluationRecord entity = new EvaluationRecord();
        entity.setId(dto.getId());
        entity.setTenantId(dto.getTenantId());
        entity.setName(dto.getName());
        entity.setStatus(dto.getStatus());
        entity.setMetadataJson(dto.getMetadataJson());
        return entity;
    }
}
