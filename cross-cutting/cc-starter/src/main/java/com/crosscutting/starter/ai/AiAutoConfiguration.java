package com.crosscutting.starter.ai;

import com.crosscutting.starter.CcProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@AutoConfiguration
@ConditionalOnProperty(name = "cc.ai.gatewayUrl")
@EnableConfigurationProperties(CcProperties.class)
@ComponentScan(basePackageClasses = AiAutoConfiguration.class)
public class AiAutoConfiguration {

    @Bean
    public RestClient aiRestClient(CcProperties ccProperties) {
        CcProperties.AiProperties ai = ccProperties.getAi();
        return RestClient.builder()
                .baseUrl(ai.getGatewayUrl())
                .defaultHeader("Authorization", "Bearer " + ai.getMasterKey())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public AiService aiService(RestClient aiRestClient) {
        return new AiService(aiRestClient);
    }
}
