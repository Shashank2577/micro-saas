package com.crosscutting.starter.payments;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ComponentScan(basePackageClasses = PaymentsAutoConfiguration.class)
public class PaymentsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(PaymentProvider.class)
    public PaymentProvider noOpPaymentProvider() {
        return new NoOpPaymentProvider();
    }
}
