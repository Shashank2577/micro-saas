package com.crosscutting.starter.storage;

import com.crosscutting.starter.CcProperties;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ConditionalOnProperty(name = "cc.storage.endpoint")
@ComponentScan(basePackageClasses = StorageAutoConfiguration.class)
public class StorageAutoConfiguration {

    @Bean
    public MinioClient minioClient(CcProperties ccProperties) {
        CcProperties.StorageProperties storage = ccProperties.getStorage();
        return MinioClient.builder()
                .endpoint(storage.getEndpoint())
                .credentials(storage.getAccessKey(), storage.getSecretKey())
                .build();
    }
}
