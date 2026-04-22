package com.microsaas.brandvoice.service;

import com.microsaas.brandvoice.entity.ToneOfVoice;
import com.microsaas.brandvoice.repository.ToneOfVoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ToneOfVoiceService {
    private final ToneOfVoiceRepository repository;

    public List<ToneOfVoice> findAllByTenant(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public ToneOfVoice save(ToneOfVoice entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
