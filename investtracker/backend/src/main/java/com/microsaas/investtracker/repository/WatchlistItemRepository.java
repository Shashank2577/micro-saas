package com.microsaas.investtracker.repository;

import com.microsaas.investtracker.entity.WatchlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WatchlistItemRepository extends JpaRepository<WatchlistItem, UUID> {
    List<WatchlistItem> findByTenantIdAndWatchlistId(UUID tenantId, UUID watchlistId);
}
