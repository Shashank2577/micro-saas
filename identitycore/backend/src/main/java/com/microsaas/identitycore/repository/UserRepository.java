package com.microsaas.identitycore.repository;

import com.microsaas.identitycore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findByTenantId(UUID tenantId);
    Optional<User> findByIdAndTenantId(UUID id, UUID tenantId);
}
