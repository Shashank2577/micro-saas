package com.crosscutting.starter.security;

import com.crosscutting.starter.CcProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;

@AutoConfiguration
@EnableConfigurationProperties(CcProperties.class)
public class SecurityAutoConfiguration {

    @Bean
    public CorsConfig corsConfig(CcProperties ccProperties) {
        return new CorsConfig(ccProperties);
    }

    @Bean
    @ConditionalOnProperty(name = "cc.security.rate-limit-enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnBean(StringRedisTemplate.class)
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilterRegistration(StringRedisTemplate redisTemplate, CcProperties ccProperties) {
        FilterRegistrationBean<RateLimitFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitFilter(redisTemplate, ccProperties));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 50);
        return registrationBean;
    }

    @Bean
    @ConditionalOnProperty(name = "cc.security.encryption-key")
    public EncryptionService encryptionService(CcProperties ccProperties) {
        return new EncryptionService(ccProperties.getSecurity().getEncryptionKey());
    }
}
