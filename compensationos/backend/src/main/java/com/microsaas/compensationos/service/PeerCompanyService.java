package com.microsaas.compensationos.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.compensationos.entity.PeerCompany;
import com.microsaas.compensationos.repository.PeerCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PeerCompanyService {
    private final PeerCompanyRepository repository;

    public List<PeerCompany> getAll() {
        return repository.findByTenantId(TenantContext.require());
    }

    public PeerCompany save(PeerCompany entity) {
        entity.setTenantId(TenantContext.require());
        return repository.save(entity);
    }
}
