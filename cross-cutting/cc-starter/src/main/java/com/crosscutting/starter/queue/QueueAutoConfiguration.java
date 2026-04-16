package com.crosscutting.starter.queue;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@AutoConfiguration
@ComponentScan(basePackageClasses = QueueAutoConfiguration.class)
public class QueueAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "cc.queue.worker-enabled", havingValue = "true", matchIfMissing = true)
    public JobWorker jobWorker(QueueService queueService, List<JobHandler> handlers) {
        return new JobWorker(queueService, handlers);
    }
}
