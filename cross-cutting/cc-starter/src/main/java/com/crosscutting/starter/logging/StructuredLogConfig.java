package com.crosscutting.starter.logging;

import org.springframework.context.annotation.Configuration;

/**
 * Structured logging configuration.
 * <p>
 * Spring Boot 3.x supports structured JSON logging out of the box via
 * {@code logging.structured.format.console} and {@code logging.structured.format.file}.
 * MDC fields (correlationId, tenantId, userId) are automatically included
 * in structured log output when set via {@link CorrelationIdFilter}.
 * <p>
 * For plain-text logging, the default pattern is enhanced via
 * {@code cc-defaults.yml} to include MDC fields in the log level pattern:
 * <pre>
 * logging.pattern.level: "%5p [%X{correlationId}] [%X{tenantId}] [%X{userId}]"
 * </pre>
 */
@Configuration
public class StructuredLogConfig {
    // No custom beans needed — Spring Boot's built-in structured logging
    // automatically picks up MDC fields set by CorrelationIdFilter.
}
