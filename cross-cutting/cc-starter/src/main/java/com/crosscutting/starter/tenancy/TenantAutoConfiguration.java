package com.crosscutting.starter.tenancy;

import com.crosscutting.starter.CcProperties;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@ConditionalOnProperty(prefix = "cc.tenancy", name = "mode", matchIfMissing = true)
@EnableJpaRepositories(basePackageClasses = TenantRepository.class)
@EntityScan(basePackageClasses = Tenant.class)
public class TenantAutoConfiguration {

    @Bean
    public TenantFilter tenantFilter(CcProperties properties, TenantRepository tenantRepository,
                                     TenantMembershipRepository tenantMembershipRepository) {
        return new TenantFilter(properties, tenantRepository, tenantMembershipRepository);
    }

    @Bean
    public TenantService tenantService(TenantRepository tenantRepository) {
        return new TenantService(tenantRepository);
    }

    @Bean
    public TenantOnboardingService tenantOnboardingService(TenantRepository tenantRepository,
                                                           EntityManager entityManager) {
        return new TenantOnboardingService(tenantRepository, entityManager);
    }

    @Bean
    public TenantController tenantController(TenantService tenantService,
                                             TenantOnboardingService tenantOnboardingService) {
        return new TenantController(tenantService, tenantOnboardingService);
    }
}
