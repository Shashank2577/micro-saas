package com.crosscutting.socialintelligence.repository;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import com.crosscutting.socialintelligence.domain.PlatformAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlatformAccountRepository extends JpaRepository<PlatformAccount, UUID> {
    List<PlatformAccount> findByTenantId(String tenantId);
    Optional<PlatformAccount> findByIdAndTenantId(UUID id, String tenantId);
    Optional<PlatformAccount> findByTenantIdAndPlatformNameAndPlatformAccountId(String tenantId, String platformName, String platformAccountId);
}
