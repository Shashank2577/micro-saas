package com.crosscutting.starter.export;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@ConditionalOnProperty(name = "cc.export.enabled", havingValue = "true", matchIfMissing = true)
@ComponentScan(basePackageClasses = ExportAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = ExportAutoConfiguration.class)
@EntityScan(basePackageClasses = ExportAutoConfiguration.class)
public class ExportAutoConfiguration {
}
