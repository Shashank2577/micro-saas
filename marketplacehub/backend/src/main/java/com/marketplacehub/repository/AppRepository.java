package com.marketplacehub.repository;

import com.marketplacehub.model.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppRepository extends JpaRepository<App, UUID> {
    List<App> findByTenantId(UUID tenantId);
    List<App> findByTenantIdAndCategory(UUID tenantId, String category);

    @Query("SELECT a FROM App a WHERE a.tenantId = :tenantId AND (LOWER(a.name) LIKE LOWER(CONCAT('%', :searchText, '%')) OR LOWER(a.description) LIKE LOWER(CONCAT('%', :searchText, '%')))")
    List<App> searchApps(UUID tenantId, String searchText);
    
    List<App> findTop10ByTenantIdOrderByTotalInstallationsDesc(UUID tenantId);
}
