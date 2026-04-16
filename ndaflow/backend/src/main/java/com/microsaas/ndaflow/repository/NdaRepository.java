package com.microsaas.ndaflow.repository;

import com.microsaas.ndaflow.domain.Nda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface NdaRepository extends JpaRepository<Nda, UUID> {
    List<Nda> findByTenantId(UUID tenantId);

    @Query("SELECT n FROM Nda n WHERE n.tenantId = :tenantId AND n.expiresAt IS NOT NULL AND n.expiresAt BETWEEN :today AND :expiryDate")
    List<Nda> findExpiringSoon(@Param("tenantId") UUID tenantId, @Param("today") LocalDate today, @Param("expiryDate") LocalDate expiryDate);
}
