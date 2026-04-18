package com.microsaas.logisticsai.repository;

import com.microsaas.logisticsai.domain.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, UUID> {
    List<Route> findByTenantId(UUID tenantId);
    Optional<Route> findByIdAndTenantId(UUID id, UUID tenantId);
}
