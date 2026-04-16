package com.crosscutting.starter.rbac;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@ConditionalOnProperty(name = "cc.rbac.enabled", havingValue = "true", matchIfMissing = true)
@ComponentScan(basePackageClasses = RbacAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = RbacAutoConfiguration.class)
@EntityScan(basePackageClasses = RbacAutoConfiguration.class)
public class RbacAutoConfiguration {
}
