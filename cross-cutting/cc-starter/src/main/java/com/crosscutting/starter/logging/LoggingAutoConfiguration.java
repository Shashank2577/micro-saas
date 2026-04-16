package com.crosscutting.starter.logging;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

@AutoConfiguration
@Import(StructuredLogConfig.class)
public class LoggingAutoConfiguration {

    @Bean
    public CorrelationIdFilter correlationIdFilter() {
        return new CorrelationIdFilter();
    }

    @Bean
    public HealthController healthController(DataSource dataSource) {
        return new HealthController(dataSource);
    }
}
