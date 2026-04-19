package com.micro.interviewos.service;

import com.micro.interviewos.domain.InterviewPlan;
import com.micro.interviewos.dto.InterviewPlanDTO;
import com.micro.interviewos.repository.InterviewPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewPlanService {

    private final InterviewPlanRepository repository;

    public List<InterviewPlanDTO> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<InterviewPlanDTO> findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .map(this::toDto);
    }

    public InterviewPlanDTO create(InterviewPlanDTO dto) {
        InterviewPlan entity = toEntity(dto);
        entity = repository.save(entity);
        return toDto(entity);
    }

    public InterviewPlanDTO update(UUID id, UUID tenantId, InterviewPlanDTO dto) {
        InterviewPlan entity = repository.findByIdAndTenantId(id, tenantId)
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

    private InterviewPlanDTO toDto(InterviewPlan entity) {
        InterviewPlanDTO dto = new InterviewPlanDTO();
        dto.setId(entity.getId());
        dto.setTenantId(entity.getTenantId());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus());
        dto.setMetadataJson(entity.getMetadataJson());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private InterviewPlan toEntity(InterviewPlanDTO dto) {
        InterviewPlan entity = new InterviewPlan();
        entity.setId(dto.getId());
        entity.setTenantId(dto.getTenantId());
        entity.setName(dto.getName());
        entity.setStatus(dto.getStatus());
        entity.setMetadataJson(dto.getMetadataJson());
        return entity;
    }
}
