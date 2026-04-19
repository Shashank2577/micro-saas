package com.microsaas.debtnavigator.service;

import com.microsaas.debtnavigator.entity.DebtAccount;
import com.microsaas.debtnavigator.repository.DebtAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountsService {
    private final DebtAccountRepository repository;

    public DebtAccount create(DebtAccount account) {
        return repository.save(account);
    }

    public List<DebtAccount> list(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public Optional<DebtAccount> getById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    public DebtAccount update(DebtAccount account) {
        return repository.save(account);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public boolean validate(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId).isPresent();
    }

    public Object simulate(UUID id, UUID tenantId) {
        return "Simulation result for account " + id;
    }
}
