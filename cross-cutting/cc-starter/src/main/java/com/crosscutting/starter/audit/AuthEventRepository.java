package com.crosscutting.starter.audit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthEventRepository extends JpaRepository<AuthEvent, Long> {

    Page<AuthEvent> findByUserId(UUID userId, Pageable pageable);

    Page<AuthEvent> findByEventType(String eventType, Pageable pageable);

    Page<AuthEvent> findByTenantId(UUID tenantId, Pageable pageable);
}
