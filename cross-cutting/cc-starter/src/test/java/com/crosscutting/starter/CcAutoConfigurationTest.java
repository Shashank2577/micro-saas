package com.crosscutting.starter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class CcAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(CcAutoConfiguration.class));

    @Test
    void defaultProperties_loadCorrectly() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(CcProperties.class);
            CcProperties properties = context.getBean(CcProperties.class);

            assertThat(properties.getTenancy().getMode()).isEqualTo("multi");
            assertThat(properties.getAudit().isSystemAuditEnabled()).isTrue();
            assertThat(properties.getAudit().isLogRequestBody()).isTrue();
            assertThat(properties.getAudit().isLogResponseBody()).isFalse();
            assertThat(properties.getSecurity().getDefaultRateLimit()).isEqualTo(100);
        });
    }
}
