package com.microsaas.investtracker.repository;

import com.microsaas.investtracker.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, UUID> {
    List<Watchlist> findByTenantId(UUID tenantId);
    Optional<Watchlist> findByIdAndTenantId(UUID id, UUID tenantId);
}
