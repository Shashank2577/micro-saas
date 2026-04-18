package com.microsaas.customersuccessos.repository;

import com.microsaas.customersuccessos.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findByTenantId(UUID tenantId);
}
