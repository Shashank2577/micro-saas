package com.crosscutting.starter.flags;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@ConditionalOnProperty(name = "cc.flags.enabled", havingValue = "true", matchIfMissing = true)
@ComponentScan(basePackageClasses = FlagsAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = FlagsAutoConfiguration.class)
@EntityScan(basePackageClasses = FlagsAutoConfiguration.class)
public class FlagsAutoConfiguration {
}
