package com.crosscutting.starter;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(CcProperties.class)
public class CcAutoConfiguration {
    // Module-specific @Import annotations will be added as modules are built
}
