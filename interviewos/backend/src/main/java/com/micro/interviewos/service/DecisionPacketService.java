package com.micro.interviewos.service;

import com.micro.interviewos.domain.DecisionPacket;
import com.micro.interviewos.dto.DecisionPacketDTO;
import com.micro.interviewos.repository.DecisionPacketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DecisionPacketService {

    private final DecisionPacketRepository repository;

    public List<DecisionPacketDTO> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<DecisionPacketDTO> findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .map(this::toDto);
    }

    public DecisionPacketDTO create(DecisionPacketDTO dto) {
        DecisionPacket entity = toEntity(dto);
        entity = repository.save(entity);
        return toDto(entity);
    }

    public DecisionPacketDTO update(UUID id, UUID tenantId, DecisionPacketDTO dto) {
        DecisionPacket entity = repository.findByIdAndTenantId(id, tenantId)
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

    private DecisionPacketDTO toDto(DecisionPacket entity) {
        DecisionPacketDTO dto = new DecisionPacketDTO();
        dto.setId(entity.getId());
        dto.setTenantId(entity.getTenantId());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus());
        dto.setMetadataJson(entity.getMetadataJson());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private DecisionPacket toEntity(DecisionPacketDTO dto) {
        DecisionPacket entity = new DecisionPacket();
        entity.setId(dto.getId());
        entity.setTenantId(dto.getTenantId());
        entity.setName(dto.getName());
        entity.setStatus(dto.getStatus());
        entity.setMetadataJson(dto.getMetadataJson());
        return entity;
    }
}
