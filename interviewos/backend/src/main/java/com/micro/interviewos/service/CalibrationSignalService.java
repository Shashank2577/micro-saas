package com.micro.interviewos.service;

import com.micro.interviewos.domain.CalibrationSignal;
import com.micro.interviewos.dto.CalibrationSignalDTO;
import com.micro.interviewos.repository.CalibrationSignalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalibrationSignalService {

    private final CalibrationSignalRepository repository;

    public List<CalibrationSignalDTO> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<CalibrationSignalDTO> findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .map(this::toDto);
    }

    public CalibrationSignalDTO create(CalibrationSignalDTO dto) {
        CalibrationSignal entity = toEntity(dto);
        entity = repository.save(entity);
        return toDto(entity);
    }

    public CalibrationSignalDTO update(UUID id, UUID tenantId, CalibrationSignalDTO dto) {
        CalibrationSignal entity = repository.findByIdAndTenantId(id, tenantId)
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

    private CalibrationSignalDTO toDto(CalibrationSignal entity) {
        CalibrationSignalDTO dto = new CalibrationSignalDTO();
        dto.setId(entity.getId());
        dto.setTenantId(entity.getTenantId());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus());
        dto.setMetadataJson(entity.getMetadataJson());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private CalibrationSignal toEntity(CalibrationSignalDTO dto) {
        CalibrationSignal entity = new CalibrationSignal();
        entity.setId(dto.getId());
        entity.setTenantId(dto.getTenantId());
        entity.setName(dto.getName());
        entity.setStatus(dto.getStatus());
        entity.setMetadataJson(dto.getMetadataJson());
        return entity;
    }
}
