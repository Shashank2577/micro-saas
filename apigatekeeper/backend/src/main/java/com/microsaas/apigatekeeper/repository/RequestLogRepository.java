package com.microsaas.apigatekeeper.repository;

import com.microsaas.apigatekeeper.entity.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLog, UUID> {
    List<RequestLog> findByTenantId(String tenantId);
}
