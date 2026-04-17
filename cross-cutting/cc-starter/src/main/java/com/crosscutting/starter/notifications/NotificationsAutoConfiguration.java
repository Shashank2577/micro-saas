package com.crosscutting.starter.notifications;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@ConditionalOnProperty(name = "cc.notifications.enabled", havingValue = "true", matchIfMissing = true)
@ComponentScan(basePackageClasses = NotificationsAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = NotificationsAutoConfiguration.class)
@EntityScan(basePackageClasses = NotificationsAutoConfiguration.class)
public class NotificationsAutoConfiguration {
}
