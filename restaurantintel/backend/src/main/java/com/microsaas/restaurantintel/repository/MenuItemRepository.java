package com.microsaas.restaurantintel.repository;

import com.microsaas.restaurantintel.domain.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {
    List<MenuItem> findByTenantId(UUID tenantId);
}
