package com.crosscutting.starter.audit;

import com.crosscutting.starter.CcProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@EntityScan(basePackageClasses = SystemAuditLog.class)
@EnableJpaRepositories(basePackageClasses = SystemAuditLogRepository.class)
@ComponentScan(basePackageClasses = AuditAutoConfiguration.class)
public class AuditAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "cc.audit.systemAuditEnabled", havingValue = "true", matchIfMissing = true)
    public SystemAuditFilter systemAuditFilter(SystemAuditLogRepository repository,
                                               RequestBodySanitizer sanitizer,
                                               CcProperties properties,
                                               ObjectMapper objectMapper) {
        return new SystemAuditFilter(repository, sanitizer, properties, objectMapper);
    }
}
