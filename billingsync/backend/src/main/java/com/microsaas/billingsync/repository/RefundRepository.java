package com.microsaas.billingsync.repository;

import com.microsaas.billingsync.model.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RefundRepository extends JpaRepository<Refund, UUID> {
    List<Refund> findByTenantId(String tenantId);
}
