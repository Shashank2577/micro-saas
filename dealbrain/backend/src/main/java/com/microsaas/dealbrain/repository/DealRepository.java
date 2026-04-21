package com.microsaas.dealbrain.repository;

import com.microsaas.dealbrain.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DealRepository extends JpaRepository<Deal, UUID> {
    List<Deal> findByTenantId(UUID tenantId);
}
