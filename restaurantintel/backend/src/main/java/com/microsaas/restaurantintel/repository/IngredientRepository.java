package com.microsaas.restaurantintel.repository;

import com.microsaas.restaurantintel.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {
    List<Ingredient> findByTenantId(UUID tenantId);
}
