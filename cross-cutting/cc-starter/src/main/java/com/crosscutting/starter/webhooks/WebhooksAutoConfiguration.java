package com.crosscutting.starter.webhooks;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@ConditionalOnProperty(name = "cc.webhooks.enabled", havingValue = "true", matchIfMissing = true)
@ComponentScan(basePackageClasses = WebhooksAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = WebhooksAutoConfiguration.class)
@EntityScan(basePackageClasses = WebhooksAutoConfiguration.class)
public class WebhooksAutoConfiguration {
}
