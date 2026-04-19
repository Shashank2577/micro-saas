package com.microsaas.debtnavigator.service;

import com.microsaas.debtnavigator.entity.MilestoneTrack;
import com.microsaas.debtnavigator.repository.MilestoneTrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MilestonesService {
    private final MilestoneTrackRepository repository;

    public MilestoneTrack create(MilestoneTrack track) {
        return repository.save(track);
    }

    public List<MilestoneTrack> list(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public Optional<MilestoneTrack> getById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    public MilestoneTrack update(MilestoneTrack track) {
        return repository.save(track);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public boolean validate(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId).isPresent();
    }

    public Object simulate(UUID id, UUID tenantId) {
        return "Simulation result for milestone track " + id;
    }
}
