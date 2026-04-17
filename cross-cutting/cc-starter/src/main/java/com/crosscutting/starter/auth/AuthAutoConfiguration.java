package com.crosscutting.starter.auth;

import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.web.SecurityFilterChain;

@AutoConfiguration
@ConditionalOnProperty(prefix = "cc.auth", name = "realm", matchIfMissing = true)
@Import(SecurityConfig.class)
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@EntityScan(basePackageClasses = User.class)
public class AuthAutoConfiguration {

    @Bean
    public UserSyncService userSyncService(UserRepository userRepository, EntityManager entityManager) {
        return new UserSyncService(userRepository, entityManager);
    }

    @Bean
    public UserSyncFilter userSyncFilter(UserSyncService userSyncService) {
        return new UserSyncFilter(userSyncService);
    }

    @Bean
    public AuthController authController(UserSyncService userSyncService) {
        return new AuthController(userSyncService);
    }
}
